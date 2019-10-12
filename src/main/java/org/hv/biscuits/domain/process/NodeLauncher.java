package org.hv.biscuits.domain.process;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wujianchuan
 */
@Component
@Order(5)
public class NodeLauncher implements CommandLineRunner {
    private final List<Node> nodeList;

    public NodeLauncher(@Nullable List<Node> nodeList) {
        this.nodeList = nodeList;
    }

    @Override
    public void run(String... args) throws Exception {
        // 将所有节点(Node)分组并创建作用域（Context） 将所有的作用域（Context）放入作用域工厂（ContextFactory）
        if (nodeList != null) {
            Map<String, List<Node>> nodesMapper = nodeList.parallelStream().collect(Collectors.groupingBy(node -> {
                Transfer transfer = node.getClass().getAnnotation(Transfer.class);
                return transfer.processorName();
            }));

            ContextFactory contextFactory = ContextFactory.getInstance();
            nodesMapper.entrySet().parallelStream()
                    .forEach(entry -> {
                        List<Node> nodeList = entry.getValue();
                        Map<String, Node> nodeMapper = new HashMap<>();
                        Transfer transfer;
                        for (Node node : nodeList) {
                            transfer = node.getClass().getAnnotation(Transfer.class);
                            nodeMapper.put(transfer.nodeName(), node);
                        }
                        Context context = new ProcessContext(nodeMapper);
                        contextFactory.putContext(entry.getKey(), context);
                    });
        }
    }
}
