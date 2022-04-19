package services;

import java.util.List;

import javax.persistence.NoResultException;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.FollowConverter;
import actions.views.FollowView;
import constants.JpaConst;
import models.Employee;
import models.Follow;

/**
 * フォローテーブルの操作に関わる処理を行うクラス
 */
public class FollowService extends ServiceBase {

    /**
     * 指定されたページ数の一覧画面に表示するデータを取得し、FollowViewのリストで返却する
     * @param page ページ数
     * @return 表示するデータのリスト
     */
    public List<FollowView> getPerPage(int page) {
        List<Follow> followings = em.createNamedQuery(JpaConst.Q_FOL_GET_ALL_FOLLOWING, Follow.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();

        return FollowConverter.toViewList(followings);
    }

    /**★ここ今やってます
     * 指定されたページ数の一覧画面に表示するフォローデータを取得し、ReportViewのリストで返却する
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<FollowView> getAllFollowigPerPage(int page, Employee following) {

        List<Follow> followings = em.createNamedQuery(JpaConst.Q_FOL_GET_ALL_FOLLOWING, Follow.class)
                .setParameter(JpaConst.JPQL_PARM_FOLLOWING, following)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return FollowConverter.toViewList(followings);
    }

    /**
     * 指定されたページ数の一覧画面に表示するフォロワーデータを取得し、ReportViewのリストで返却する
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<FollowView> getAllFollowerPerPage(int page, Employee follower) {

        List<Follow> followers = em.createNamedQuery(JpaConst.Q_FOL_GET_ALL_FOLLOWER, Follow.class)
                .setParameter(JpaConst.JPQL_PARM_FOLLOWER, follower)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return FollowConverter.toViewList(followers);
    }

    /**
     * フォローテーブルのフォローしているデータの件数を取得し、返却する
     * @return フォローテーブルのフォローしているデータの件数
     */
    public long countAllFollowing(Employee following) {
        long followingCount = (long) em.createNamedQuery(JpaConst.Q_FOL_COUNT_FOLLOWING, Long.class)
                .setParameter(JpaConst.JPQL_PARM_FOLLOWING, following)
                .getSingleResult();

        return followingCount;
    }

    /**
     * フォローテーブルのフォロワーの件数を取得し、返却する
     * @return フォローテーブルのフォロワーのデータの件数
     */
    public long countAllFollower(Employee follower) {
        long followerCount = (long) em.createNamedQuery(JpaConst.Q_FOL_COUNT_FOLLOWER, Long.class)
                .setParameter(JpaConst.JPQL_PARM_FOLLOWER, follower)
                .getSingleResult();

        return followerCount;
    }

    /**
     * idを条件に取得したデータをFollowViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public FollowView findOne(int id) {
        Follow f = findOneInternal(id);
        return FollowConverter.toView(f);
    }

    /**
     * idを条件に取得したデータをEmployeeViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public EmployeeView findOneEmployee(int id) {
        Employee e = findOneInternalEmployee(id);
        return EmployeeConverter.toView(e);
    }

    /**
     * idを条件にデータを1件取得し、Employeeのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    private Employee findOneInternalEmployee(int id) {
        Employee e = em.find(Employee.class, id);

        return e;
    }


    /**
     * idを条件にデータを1件取得し、Followのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    private Follow findOneInternal(int id) {
        Follow f = em.find(Follow.class, id);

        return f;
    }

    /**
     * フォローデータを1件登録する
     * @param fv フォローデータ
     *
     */
     public void create(FollowView fv) {
        em.getTransaction().begin();
        em.persist(FollowConverter.toModel(fv));
        em.getTransaction().commit();
    }

    /**
     * フォローを外す
     * @param fv 画面に表示された従業員の登録内容
     */
    public void destroy(Follow f) {
        em.getTransaction().begin();
        em.remove(f);       // データ削除
        em.getTransaction().commit();
        em.close();
    }

    /**
     * idを条件にフォローテーブルを検索、検索結果を返却する
     * @param フォローしている人のid・フォローされる人のid
     * @return 検索結果(フォローしている:true フォローしていない:false)
     */
    public Follow findFollow(Employee following, Employee follower) {
        Follow follow = null;
        try {
            follow = em.createNamedQuery(JpaConst.Q_FOL_GET_FOLLOWER, Follow.class)
                        .setParameter(JpaConst.JPQL_PARM_FOLLOWER, follower)
                        .setParameter(JpaConst.JPQL_PARM_FOLLOWING, following)
                        .getSingleResult();
        } catch (NoResultException ex) {
            follow = null;
        }

        return follow;
    }




}