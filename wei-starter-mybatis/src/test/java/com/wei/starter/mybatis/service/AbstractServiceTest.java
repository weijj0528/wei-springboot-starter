package com.wei.starter.mybatis.service;

import com.wei.starter.mybatis.xmapper.XMapper;
import org.apache.ibatis.executor.BatchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * AbstractService#insertList 单元测试
 * <p>
 * 覆盖返回值修复: MySQL 批量插入返回 SUCCESS_NO_INFO(-2) 时，insertList 必须按条数返回，
 * 而不是累加 BatchResult 的 updateCounts。
 */
@DisplayName("AbstractService#insertList")
class AbstractServiceTest {

    private XMapper<String> mapper;

    private TestService service;

    @BeforeEach
    void setUp() {
        mapper = mock(XMapper.class);
        service = new TestService(mapper);
    }

    @Test
    @DisplayName("BatchResult updateCounts 为 -2(SUCCESS_NO_INFO) 时仍返回 list.size()")
    void insertList_returnsListSize_whenBatchReturnsSuccessNoInfo() {
        List<String> list = Arrays.asList("a", "b", "c");

        BatchResult batchResult = mock(BatchResult.class);
        when(batchResult.getUpdateCounts()).thenReturn(new int[]{-2, -2, -2});
        when(mapper.insert(list)).thenReturn(List.of(batchResult));

        int result = service.insertList(list);

        assertEquals(list.size(), result);
        verify(mapper).insert(list);
    }

    @Test
    @DisplayName("空列表返回 0")
    void insertList_emptyList_returnsZero() {
        List<String> list = List.of();
        when(mapper.insert(list)).thenReturn(List.of());

        int result = service.insertList(list);

        assertEquals(0, result);
    }

    @Test
    @DisplayName("单元素列表返回 1")
    void insertList_singleElement_returnsOne() {
        List<String> list = List.of("only");
        BatchResult batchResult = mock(BatchResult.class);
        when(batchResult.getUpdateCounts()).thenReturn(new int[]{1});
        when(mapper.insert(list)).thenReturn(List.of(batchResult));

        int result = service.insertList(list);

        assertEquals(1, result);
    }

    /**
     * AbstractService 的具体测试子类，绕过 Spring 注入，直接返回 mock mapper。
     */
    static class TestService extends AbstractService<String> {

        private final XMapper<String> mapper;

        TestService(XMapper<String> mapper) {
            this.mapper = mapper;
        }

        @Override
        public XMapper<String> getMapper() {
            return mapper;
        }
    }
}
