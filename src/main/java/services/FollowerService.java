package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.FollowerConverter;
import actions.views.FollowerView;
import constants.JpaConst;
import models.Follower;

/**
 * フォロワーテーブルの操作に関わる処理を行うクラス
 *
 */
public class FollowerService extends ServiceBase {

    /**
     * 指定した従業員が作成したフォロワーデータを、指定されたページ数の一覧画面に表示する分取得しFollowerViewのリストで返却する
     * @param employee 従業員
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<FollowerView> getMinePerPage(EmployeeView employee, int page) {

        List<Follower> followers = em.createNamedQuery(JpaConst.Q_FLW_GET_ALL_MINE, Follower.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .getResultList();
        return FollowerConverter.toViewList(followers);
    }


    /**
     * 指定した従業員が作成したフォロワーデータの件数を取得し、返却する
     * @param employee
     * @return フォロワーデータの件数
     */
    public long countAllMine(EmployeeView employee) {

        long count = (long) em.createNamedQuery(JpaConst.Q_FLW_COUNT_ALL_MINE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getSingleResult();

        return count;
    }


    /**
     * idを条件に取得したデータをFollowerViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public FollowerView findOne(int id) {
        return FollowerConverter.toView(findOneInternal(id));
    }


    /**
     * フォロワーの登録内容を元にデータを1件作成し、フォロワーテーブルに登録する
     * @param fv フォロワーの登録内容
     * バリデーションは後から作成
     */
    public void create(FollowerView fv) {
        LocalDateTime ldt = LocalDateTime.now();
        fv.setCreatedAt(ldt);
        fv.setUpdatedAt(ldt);
        createInternal(fv);
        }


    /**
     * アップデートは後から作成
     */


    /**
     * idを条件にデータを1件取得する
     * @param id
     * @return 取得データのインスタンス
     */
    private Follower findOneInternal(int id) {
        return em.find(Follower.class, id);
    }


    /**
     * フォロワーデータを1件登録する
     * @param fv フォロワーデータ
     */
    private void createInternal(FollowerView fv) {

        em.getTransaction().begin();
        em.persist(FollowerConverter.toModel(fv));
        em.getTransaction().commit();
    }
}
