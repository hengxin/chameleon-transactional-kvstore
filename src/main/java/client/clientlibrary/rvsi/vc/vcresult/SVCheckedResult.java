package client.clientlibrary.rvsi.vc.vcresult;

import client.clientlibrary.rvsi.vc.SVVersionConstraint;

/**
 * {@link SVCheckedResult} records the checked result of {@link SVVersionConstraint}.
 *
 * Created by hengxin on 16-9-22.
 */
public class SVCheckedResult extends VCCheckedResult {
    private static final long serialVersionUID = -2583990169731170050L;

    public SVCheckedResult(boolean svChecked) {
        super(true, true, svChecked);
    }
}
