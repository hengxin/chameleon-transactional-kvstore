package network.membership;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A master needs to know itself and all of <i>its</i> slaves.
 * However, in this implementation, a master does not necessarily know other masters
 * and their slaves.
 * 
 * @author hengxin
 * @date Created on 12-03-2015
 */
public final class MasterMembership extends AbstractStaticMembership {

	private final static Logger LOGGER = LoggerFactory.getLogger(MasterMembership.class);
	
	@Nonnull private List<Member> slaves;

	public MasterMembership(@Nonnull String file) {
		super(file);
	}

	/**
	 * Only one line to parse: master = slave, slave, ... 
	 * <p> The system exits if an error occurs during parse 
	 * 	(maybe due to ill-formated file or master parse error).
	 */
	@Override
	public void parseMembershipFromProp() {
		if (super.prop.size() != 1) {
			LOGGER.error("Failed to parse membership from [%s]: It should a single line of the (master = slave, slave, ...) format.", super.file);
			System.exit(1);	// fail fast
		}
		
		String master = super.prop.stringPropertyNames().toArray(new String[1])[0];
		String slaves = super.prop.getProperty(master);

		// parse the master itself
		Optional<Member> master_opt = Member.parseMember(master);
		if(! master_opt.isPresent()) {
			LOGGER.error("Cannot parse this master [{}] itself.", master);
			System.exit(1);	// fail fast
		}
		super.self = master_opt.get();
		
		// parse slaves
		this.slaves = Member.parseMembers(slaves);

		LOGGER.info("I am a master: {}. My slaves are: {}.", master, slaves);
	}

	public @Nonnull List<Member> getSlaves() {
		return slaves;
	}
}
