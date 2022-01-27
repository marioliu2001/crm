package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.workbench.dao.ClueDao;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description TODO
 * @Date 2022/1/25 18:17
 * @Version 1.0
 */
@Service
public class ClueServiceImpl implements ClueService {

    @Autowired
    private ClueDao clueDao;

    @Override
    public List<Clue> getClueList() {
        return clueDao.findAllClue();
    }

    @Override
    public long getTotalCounts() {
        return clueDao.getTotalCounts();
    }

    @Override
    public List<Clue> getClueListByPage(int pageNoIndex, Integer pageSize) {
        return clueDao.getClueListByPage(pageNoIndex,pageSize);
    }

    @Override
    public List<Clue> getClueListByPageCondition(int pageNoIndex, Integer pageSize, String name, String company, String phone, String source, String owner, String mphone, String state) {
        return clueDao.getClueListByPageCondition(pageNoIndex,
                  pageSize,
                  name,
                  company,
                  phone,
                  source,
                  owner,
                  mphone,
                  state );
    }

    @Override
    public long getTotalCountsCondition(String name, String company, String phone, String source, String owner, String mphone, String state) {
        return clueDao.getTotalCountsCondition(name,
                company,
                phone,
                source,
                owner,
                mphone,
                state);
    }

    @Override
    public boolean saveClue(Clue clue) {
        return clueDao.saveClue(clue);
    }

    @Override
    public Clue getClueByClueId(String id) {
        return clueDao.getClueByClueId(id);
    }

    @Override
    public boolean updateClueById(Clue clue) {
        return clueDao.updateClueById(clue);
    }

    @Override
    public void batchDeleteClueById(String[] clueIds) throws AjaxRequestException {
        for (String id : clueIds) {
            boolean flag = clueDao.deleteClueById(id);

            if (!flag)
                throw new AjaxRequestException("删除失败...");
        }
    }

}
