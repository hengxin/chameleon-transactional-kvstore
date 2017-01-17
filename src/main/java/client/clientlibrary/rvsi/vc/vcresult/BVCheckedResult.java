package client.clientlibrary.rvsi.vc.vcresult;

import client.clientlibrary.rvsi.vc.BVVersionConstraint;

/**
 * {@link BVCheckedResult} records the checked result for {@link BVVersionConstraint}.
 *
 * Created by hengxin on 16-9-22.
 */
public class BVCheckedResult extends VCCheckedResult {
    private static final long serialVersionUID = 5049571099380504250L;

    public BVCheckedResult(boolean bvChecked) {
        super(bvChecked, true, true);
    }
}
