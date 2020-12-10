package org.hv.biscuits.spine.utils;

import org.hv.biscuits.annotation.Affairs;
import org.hv.biscuits.annotation.Service;
import org.hv.biscuits.service.AbstractService;
import org.hv.biscuits.spine.model.Department;
import org.hv.biscuits.spine.model.DepartmentClassRelation;
import org.hv.biscuits.spine.model.Pair;
import org.hv.biscuits.spine.model.TreeNode;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.criteria.Restrictions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author leyan95
 */
@Service(session = "biscuits")
public class DepartmentQueryUtil extends AbstractService {

    @Affairs(on = false)
    public Department finOne(String departmentUuid) throws SQLException {
        return this.getSession().findOne(Department.class, departmentUuid);
    }

    /**
     * 获取部门树
     *
     * @param stationUuid 站数据标识
     * @return 部门树
     */
    @Affairs(on = false)
    public List<TreeNode> getDepartmentTreeByStationUuid(String stationUuid) {
        return this.getDepartmentTreeByStationUuidAndDeptClassUuid(stationUuid, null);
    }

    /**
     * 获取部门树
     *
     * @param stationUuid 站数据标识
     * @return 部门树
     */
    @Affairs(on = false)
    public List<TreeNode> getDepartmentTreeByStationUuidAndDeptClassUuid(String stationUuid, String deptClassUuid) {
        List<Department> departments;
        if (deptClassUuid != null) {
            departments = this.loadByDeptClassUuid(deptClassUuid);
        } else {
            departments = this.getSession().list(Department.class);
        }
        return this.generateDepartmentTree(
                departments.parallelStream()
                        .sorted(Comparator.comparing(Department::getSort))
                        .peek(department -> {
                            if (!department.getEnable()) {
                                department.setName(department.getName() + "(停用)");
                            }
                        }).collect(Collectors.toList()),
                stationUuid
        );
    }

    /**
     * 获取部门下拉列表
     *
     * @param stationUuid 站标识
     * @return 部门下拉集合
     */
    @Affairs(on = false)
    public List<Pair> pairsDepartment(String stationUuid) {
        List<Department> departments = this.getSession().list(Department.class);
        return departments.stream()
                .filter(department -> department.getStationUuid().equals(stationUuid))
                .map(department -> new Pair(department.getName(), department.getUuid()))
                .collect(Collectors.toList());
    }

    private List<TreeNode> generateDepartmentTree(List<Department> departments, String parentId) {
        return departments.stream().filter(department -> department.getParentUuid().equals(parentId))
                .map(department -> {
                    TreeNode treeNode = new TreeNode();
                    treeNode.setLabel(department.getName());
                    treeNode.setValue(department.getUuid());
                    List<TreeNode> treeNodes1 = generateDepartmentTree(departments, department.getUuid());
                    if (treeNodes1.size() > 0) {
                        treeNode.setChildren(treeNodes1);
                    }
                    return treeNode;
                }).collect(Collectors.toList());
    }

    /**
     * 获取部门列表
     *
     * @param deptClassUuid 部门类型数据标识
     * @return 部门列表
     */
    @Affairs(on = false)
    public List<Department> loadByDeptClassUuid(String deptClassUuid) {
        if (deptClassUuid != null) {
            Criteria criteria = this.getSession().createCriteria(DepartmentClassRelation.class);
            criteria.add(Restrictions.equ("classUuid", deptClassUuid));
            List<DepartmentClassRelation> departmentClassRelations = criteria.list();
            List<String> departmentUuids = departmentClassRelations.stream().map(DepartmentClassRelation::getDepartmentUuid).collect(Collectors.toList());
            if (departmentUuids.isEmpty()) {
                return new ArrayList<>();
            }
            return this.getSession().createCriteria(Department.class).add(Restrictions.in("uuid", departmentUuids)).list();
        } else {
            return this.getSession().list(Department.class);
        }
    }
}
