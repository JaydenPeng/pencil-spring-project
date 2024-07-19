package org.pencil.config.sensitive;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mybatis.mate.params.IParamsProcessor;
import mybatis.mate.params.SensitiveWordsProcessor;
import org.ahocorasick.trie.Emit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.List;

/**
 * @author pencil
 * @Date 24/07/19
 */
@Slf4j
@AllArgsConstructor
public class SensitiveConfig {

    @Value("${pencil.mybatis.sensitive.words:sb,cnm}")
    private List<String> sensitiveWords;

    @Bean
    public IParamsProcessor paramsProcessor() {
        return new SensitiveWordsProcessor() {

            @Override
            public List<String> loadSensitiveWords() {
                // 这里的敏感词可以从数据库中读取，也可以本文方式获取，加载只会执行一次
                return sensitiveWords;
            }

            @Override
            public String handle(String fieldName, String fieldValue, Collection<Emit> emits) {
                if (CollectionUtils.isNotEmpty(emits)) {
                    try {
                        // 这里可以过滤直接删除敏感词，也可以返回错误，提示界面删除敏感词
                        log.warn("发现敏感词（{} = {}）存在敏感词：{}", fieldName, fieldValue, JSONUtil.toJsonStr(emits));
                        String fv = fieldValue;
                        for (Emit emit : emits) {
                            fv = fv.replaceAll(emit.getKeyword(), "");
                        }
                        return fv;
                    } catch (Exception e) {
                        log.error("处理敏感词失败,fieldName={}, fieldValue={}", fieldName, fieldValue, e);
                    }
                }
                return fieldValue;
            }
        };
    }

}
