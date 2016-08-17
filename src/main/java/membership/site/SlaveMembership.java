package membership.site;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import javax.annotation.Nonnull;

/**
 * A slave needs to know itself and <i>its</i> master (not all the masters). 
 * 
 * @author hengxin
 * @date Created on 12-03-2015
 */
@Deprecated
public final class SlaveMembership extends AbstractStaticMembership {

	private final static Logger LOGGER = LoggerFactory.getLogger(SlaveMembership.class);
	
	private Optional<Member> master;
	
	public SlaveMembership(@Nonnull String file) {
		super(file);
	}

	/**
	 * Only one line to parseReplGrps: slave = master
	 * <p> The system exits if an error occurs during parseReplGrps
	 * 	(maybe due to ill-formated file or slave itself parseReplGrps error).
	 */
//	@Override
//	public void parseMembershipFromProp() {
//		if (super.prop.size() != 1) {
//			LOGGER.error("Failed to parseReplGrps membership from [%s]: It should a single line of the (slave = master) format.", super.file);
//			System.exit(1);	// fail fast
//		}
//
//		String slave = super.prop.stringPropertyNames().toArray(new String[1])[0];
//		String master = super.prop.getProperty(slave);
//
//		// parseReplGrps the slave itself
//		Optional<Member> slave_opt = Member.parseMember(slave);
//		if (! slave_opt.isPresent()) {
//			LOGGER.error("Cannot parseReplGrps this slave [{}] itself.", slave_opt);
//			System.exit(1);	// fail fast
//		}
//		super.self = slave_opt.get();
//
//		// parseReplGrps the master of this slave
//		this.master = Member.parseMember(master);
//		if (! this.master.isPresent()) {
//			LOGGER.warn("Cannot parseReplGrps my [{}] master [{}]", slave, master);
////			System.exit(1);	// fail fast
//		}
//
//		LOGGER.info("I am a slave: [{}]. My master is: [{}].", slave, master);
//	}

	public Optional<Member> getMaster() {
		return master;
	}

}
