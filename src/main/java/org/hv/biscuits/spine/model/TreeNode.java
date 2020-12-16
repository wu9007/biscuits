package org.hv.biscuits.spine.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.hv.pocket.annotation.View;

import java.util.List;

/**
 * @author wujianc
 */
@View
@JsonInclude(Include.NON_NULL)
public class TreeNode extends Pair {

    private static final long serialVersionUID = 4169755587846928569L;
    private List<TreeNode> children;
    private boolean leaf;
    private boolean checked;

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
