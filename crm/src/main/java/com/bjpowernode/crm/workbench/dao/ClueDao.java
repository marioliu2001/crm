package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    Clue toDetail(String id);

    List<ClueRemark> getClueRemarkList(String clueId);

    List<Map<String, String>> getActivityRelationListMap(String clueId);

    boolean deleteClueActivityRelation(String carId);

    List<Activity> getActivityUnRelationList(String clueId);

    List<Activity> searchLikeActivityUnRelationList(@Param("clueId") String clueId,
                                                    @Param("activityName") String activityName);


    boolean saveClueActivityRelation(List<ClueActivityRelation> carList);

    boolean deleteById(String clueId);
}
