package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Clue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClueDao {

    List<Clue> findAllClue();

    long getTotalCounts();

    List<Clue> getClueListByPage(
                                @Param("pageNo") int pageNoIndex,
                                @Param("pageSize") Integer pageSize);

    List<Clue> getClueListByPageCondition(@Param("pageNo") int pageNoIndex,
                                          @Param("pageSize") Integer pageSize,
                                          @Param("name") String name,
                                          @Param("company") String company,
                                          @Param("phone") String phone,
                                          @Param("source") String source,
                                          @Param("owner") String owner,
                                          @Param("mphone") String mphone,
                                          @Param("state") String state);

    long getTotalCountsCondition(@Param("name") String name,
                                 @Param("company") String company,
                                 @Param("phone") String phone,
                                 @Param("source") String source,
                                 @Param("owner") String owner,
                                 @Param("mphone") String mphone,
                                 @Param("state") String state);

    boolean saveClue(Clue clue);

    Clue getClueByClueId(String id);

    boolean updateClueById(Clue clue);

    boolean deleteClueById(String id);
}
