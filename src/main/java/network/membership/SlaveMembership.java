package network.membership;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A slave needs to know itself and <i>its</i> master (not all the masters). 
 * 
 * @author hengxin
 * @date Created on 12-03-2015
 */
public final class SlaveMembership extends AbstractStaticMembership {

	private final static Logger LOGGER = LoggerFactory.getLogger(SlaveMembership.class);
	
	private Optional<Member> master;
	
	public SlaveMembership(@Nonnull String file) {
		super(file);
	}

	/**
	 * Only one line to parse: slave = master
	 * <p> The system exits if an error occurs during parse 
	 * 	(maybe due to ill-formated file or slave itself parse error).
	 */
	@Override
	public void parseMembershipFromProp() {
		if (super.prop.size() != 1) {
			LOGGER.error("Failed to parse membership from [%s]: It should a single line of the (slave = master) format.", super.file);
			System.exit(1);	// fail fast
		}

		String slave = super.prop.stringPropertyNames().toArray(new String[1])[0];
		String master = super.prop.getProperty(slave);
		
		// parse the slave itself
		Optional<Member> slave_opt = Member.parseMember(slave);
		if (! slave_opt.isPresent()) {
			LOGGER.error("Cannot parse this slave [{}] itself.", slave_opt);
			System.exit(1);	// fail fast
		}
		super.self = slave_opt.get();
		
		// parse the master of this slave
		this.master = Member.parseMember(master);
		if (! this.master.isPresent()) {
			LOGGER.warn("Cannot parse my [{}] master [{}]", slave, master);
//			System.exit(1);	// fail fast
		}

		LOGGER.info("I am a slave: [{}]. My master is: [{}].", slave, master);
	}

	public Optional<Member> getMaster() {
		return master;
	}

}
