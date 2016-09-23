package client.clientlibrary.rvsi.vc.vcresult;

import client.clientlibrary.rvsi.vc.FVVersionConstraint;

/**
 * {@link FVCheckedResult} records the checked result of {@link FVVersionConstraint}.
 *
 * Created by hengxin on 16-9-22.
 */
public class FVCheckedResult extends VCCheckedResult {
    private static final long serialVersionUID = 4066080602262455603L;

    public FVCheckedResult(boolean fvChecked) {
        super(true, fvChecked, true);
    }
}
