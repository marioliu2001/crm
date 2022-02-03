package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.exception.TranditionRequestException;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Override
    public Clue toDetail(String id) {
        return clueDao.toDetail(id);
    }

    @Override
    public List<ClueRemark> getClueRemarkList(String clueId) {
        return clueDao.getClueRemarkList(clueId);
    }

    @Override
    public List<Map<String, String>> getActivityRelationListMap(String clueId) {
        return clueDao.getActivityRelationListMap(clueId);
    }

    @Override
    public boolean deleteClueActivityRelation(String carId) {
        return clueDao.deleteClueActivityRelation(carId);
    }

    @Override
    public List<Activity> getActivityUnRelationList(String clueId) {
        return clueDao.getActivityUnRelationList(clueId);
    }

    @Override
    public List<Activity> searchLikeActivityUnRelationList(String clueId, String activityName) {
        return clueDao.searchLikeActivityUnRelationList(clueId,activityName);
    }

    @Override
    public boolean saveClueActivityRelation(String[] activityIds, String clueId) {
        List<ClueActivityRelation> carList = new ArrayList<>();

        for (String activityId : activityIds) {

            ClueActivityRelation car = new ClueActivityRelation().setId(UUIDUtil.getUUID())
                    .setActivityId(activityId)
                    .setClueId(clueId);

            carList.add(car);
        }

        return clueDao.saveClueActivityRelation(carList);
    }

    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private ContactsDao contactsDao;
    @Autowired
    private ClueRemarkDao clueRemarkDao;
    @Autowired
    private CustomerRemarkDao customerRemarkDao;
    @Autowired
    private ContactsRemarkDao contactsRemarkDao;
    @Autowired
    private ClueActivityRelationDao clueActivityRelationDao;
    @Autowired
    private ContactsActivityRelationDao contactsActivityRelationDao;
    @Autowired
    private TranDao tranDao;
    @Autowired
    private TranHistoryDao tranHistoryDao;
    @Override
    public void saveConvert(String clueId, String flag, String owner, String createBy, String createTime, Tran tran) throws TranditionRequestException {

        //1.根据线索id，查询出线索数据
        Clue clue = clueDao.getClueByClueId(clueId);

        //2.根据线索中的客户名称，查询是否有当前的客户信息
        String customerName = clue.getCompany();
        String fullname = clue.getFullname();

        Customer customer = customerDao.findByName(customerName);

        Contacts contacts = null;

        if (customer==null){
            //3.当前没有客户信息，新增一条记录
            customer  = new Customer()
                    .setAddress(clue.getAddress())
                    .setContactSummary(clue.getContactSummary())
                    .setCreateBy(createBy)
                    .setCreateTime(createTime)
                    .setDescription(clue.getDescription())
                    .setId(UUIDUtil.getUUID())
                    .setName(customerName)
                    .setNextContactTime(clue.getNextContactTime())
                    .setOwner(owner)
                    .setPhone(clue.getPhone())
                    .setWebsite(clue.getWebsite())
                    .setEditBy(createBy)
                    .setEditTime(createTime);

            //新增
            boolean customerFlag = customerDao.insert(customer);

            if (!customerFlag)
                throw new TranditionRequestException("新增客户失败...");

            //4.新增联系人
            saveContacts(clue,owner,customer.getId(),createBy,createTime);

        }else {
            //当前客户已存在,新增一条联系人
            //由于联系人可能存在重名等问题,所以这里我们直接新增
            //然后后面,大家可以在联系人列表中(作业),进行清除操作,清除重复的联系人
            List<Contacts> contactsList = contactsDao.findByFullName(fullname);

            if (ObjectUtils.isEmpty(contactsList)){
                //新增联系人
                saveContacts(clue,owner,customer.getId(),createBy,createTime);
            }else {
                contacts = contactsList.get(0);
            }
        }

        //5.根据线索id，查询出线索的备注信息列表，完成转换
        List<ClueRemark> clueRemarkList = clueRemarkDao.findListByClueId(clueId);

        if (!ObjectUtils.isEmpty(clueRemarkList)) {

            //转换操作
            List<CustomerRemark> customerRemarkList = new ArrayList<>();
            List<ContactsRemark> contactsRemarkList = new ArrayList<>();

            for (ClueRemark clueRemark : clueRemarkList) {

                //封装到客户备注信息中
                CustomerRemark c = new CustomerRemark()
                        .setCreateBy(createBy)
                        .setCreateTime(createTime)
                        .setCustomerId(customer.getId())
                        .setEditBy(createBy)
                        .setEditTime(createTime)
                        .setEditFlag("0")
                        .setNoteContent(clueRemark.getNoteContent())
                        .setId(UUIDUtil.getUUID());

                customerRemarkList.add(c);

                //封装到联系人备注信息中
                ContactsRemark cr = new ContactsRemark()
                        .setContactsId(contacts.getId())
                        .setCreateBy(createBy)
                        .setCreateTime(createTime)
                        .setEditBy(createBy)
                        .setEditTime(createTime)
                        .setEditFlag("0")
                        .setId(UUIDUtil.getUUID())
                        .setNoteContent(clueRemark.getNoteContent());

                contactsRemarkList.add(cr);
            }

            //批量加载
            boolean customerRemarkFlag = customerRemarkDao.insert(customerRemarkList);
            if (!customerRemarkFlag)
                throw new TranditionRequestException("批量导入客户备注信息失败...");

            boolean contactsRemarkFlag = contactsRemarkDao.insert(contactsRemarkList);
            if (!contactsRemarkFlag)
                throw new TranditionRequestException("批量导入联系人备注信息失败...");
        }

        //6.根据线索id，查询出线索和市场活动的中间表数据，完成转换
        List<ClueActivityRelation> carList = clueActivityRelationDao.findListByClueId(clueId);

        if (!ObjectUtils.isEmpty(carList)) {

            //完成转换
            List<ContactsActivityRelation> cList = new ArrayList<>();

            for (ClueActivityRelation car : carList) {

                ContactsActivityRelation c = new ContactsActivityRelation()
                        .setActivityId(car.getActivityId())
                        .setContactsId(contacts.getId())
                        .setId(UUIDUtil.getUUID());

                cList.add(c);
            }

            //批量导入
            boolean carFlag =  contactsActivityRelationDao.insert(cList);

            if (!carFlag)
                throw new TranditionRequestException("批量导入联系人与市场活动关系表失败...");
        }

        //创建交易
        if ("a".equals(flag)){

            //8.新增交易和交易历史记录
            tran.setContactsId(customer.getId())
                    .setId(UUIDUtil.getUUID())
                    .setCreateBy(createBy)
                    .setCreateTime(createTime)
                    .setOwner(owner)
                    .setContactsId(contacts.getId())
                    .setContactSummary(clue.getContactSummary())
                    .setDescription(clue.getDescription())
                    .setNextContactTime(clue.getNextContactTime())
                    .setEditTime(createTime)
                    .setEditBy(createBy);
                //.setType()当前属性不再此处赋值,前端页面传递过来的参数，无需赋值
                //.setStage()
                //.setActivityId()
                //.setExpectedDate()
                //.setName()
                //.setMoney()

            //新增交易
            boolean tranFlag = tranDao.insert(tran);

            if (!tranFlag)
                throw new TranditionRequestException("新增交易失败...");

            //新增交易历史记录
            TranHistory history = new TranHistory()
                    .setTranId(tran.getId())
                    .setStage(tran.getStage())
                    .setMoney(tran.getMoney())
                    .setId(UUIDUtil.getUUID())
                    .setExpectedDate(tran.getExpectedDate())
                    .setCreateTime(tran.getCreateTime())
                    .setCreateBy(tran.getCreateBy());

            //新增交易历史记录
            boolean tranHistoryFlag = tranHistoryDao.insert(history);

            if (!tranHistoryFlag)
                throw new TranditionRequestException("新增交易历史失败...");
        }

        //9.删除线索相关数据，根据线索id进行删除
        //由于线索的数据已经转换成了客户和联系人数据，就可以删除点了

        //先删除多对多的关系，在删除一对多的关系，最后删除线索数据
        // 先删除关联关系，多方关系，再删除一方数据
        /*boolean carFlag = clueActivityRelationDao.deleteListByClueId(clueId);

        if (!carFlag)
            throw new TranditionRequestException("删除线索和市场活动关联关系失败...");

        boolean remarkFlag = clueRemarkDao.deleteListByClueId(clueId);

        if (!remarkFlag)
            throw new TranditionRequestException("删除线索备注信息失败...");

        boolean cFlag = clueDao.deleteById(clueId);

        if (!cFlag)
            throw new TranditionRequestException("删除线索失败...");*/

    }

    private Contacts saveContacts(Clue clue,String owner,String customerId,String createBy,String createTime) throws TranditionRequestException {
        Contacts contacts = new Contacts()
                .setSource(clue.getSource())
                .setOwner(owner)
                .setNextContactTime(clue.getNextContactTime())
                .setMphone(clue.getMphone())
                .setJob(clue.getJob())
                .setId(UUIDUtil.getUUID())
                .setFullname(clue.getFullname())
                .setEmail(clue.getEmail())
                .setDescription(clue.getDescription())
                .setCustomerId(customerId)
                .setCreateTime(createTime)
                .setCreateBy(createBy)
                .setAppellation(clue.getAppellation())
                .setAddress(clue.getAddress())
                .setEditBy(createBy)
                .setEditTime(createTime);
        //.setBirth()

        boolean contactsFlag = contactsDao.insert(contacts);

        if (!contactsFlag)
            throw new TranditionRequestException("新增联系人失败...");

        return contacts;
    }

}
