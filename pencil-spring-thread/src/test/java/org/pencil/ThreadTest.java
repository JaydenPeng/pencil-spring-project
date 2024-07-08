package org.pencil;

import cn.hutool.core.collection.ListUtil;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.pencil.config.ThreadConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * The type Thread test.
 *
 * @author pencil
 * @Date 24 /07/08
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ThreadConfig.class)
public class ThreadTest {

    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;


    /**
     * 多线程分批处理list集合,并等待所有线程处理完成
     * Test multi thread.
     */
    @Test
    public void testMultiThread() {

        List<String> stringlist = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15");

        List<List<String>> partition = ListUtil.partition(stringlist, 3);

//        List<Integer> numList = Collections.synchronizedList(new ArrayList<>())

        Collection<CompletableFuture<List<Integer>>> futures = new ArrayList<>();

        for (List<String> list : partition) {
            CompletableFuture<List<Integer>> future =
                    CompletableFuture.supplyAsync(() -> list.stream().map(Integer::parseInt).toList(), taskExecutor)
                    .thenApply(result -> {
                        // 可以在这里进行任何其他操作，如过滤或者其他处理
                        return result;
                    });
            futures.add(future);
        }

        // 等待所有任务完成，并汇总结果
        List<Integer> numList = futures.stream()
                .map(CompletableFuture::join) // 等待每个 CompletableFuture 完成，并获取结果
                .flatMap(List::stream) // 扁平化成一个流
                .toList(); // 收集结果

        Assertions.assertEquals(15, numList.size());
    }


}
