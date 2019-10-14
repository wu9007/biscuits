package org.hv.biscuits.spine.model;

import org.apache.commons.lang3.StringUtils;
import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.model.BaseEntity;

/**
 * @author wujianchuan
 */
@Entity(table = "T_PROCESS", tableId = 113, businessName = "未完结的流程")
public class Process extends BaseEntity {
    private static final long serialVersionUID = -3161539503737404152L;
    @Column(name = "PROCESS_ID")
    private String processId;
    @Column(name = "DATA_UUID")
    private String dataUuid;
    @Column(name = "CURRENT_NODE_ID")
    private String currentNodeId;
    @Column(name = "CUSTOMIZED_PROCESSES")
    private String customizedProcesses;

    public Process() {
    }

    public static Process newInstance(String processId, String dataUuid, String currentNodeId, String[] customizedProcesses) {
        Process process = new Process();
        process.setProcessId(processId);
        process.setDataUuid(dataUuid);
        process.setCurrentNodeId(currentNodeId);
        process.setCustomizedProcesses(StringUtils.join(customizedProcesses, ","));
        return process;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getDataUuid() {
        return dataUuid;
    }

    public void setDataUuid(String dataUuid) {
        this.dataUuid = dataUuid;
    }

    public String getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(String currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public String getCustomizedProcesses() {
        return customizedProcesses;
    }

    public void setCustomizedProcesses(String customizedProcesses) {
        this.customizedProcesses = customizedProcesses;
    }
}
