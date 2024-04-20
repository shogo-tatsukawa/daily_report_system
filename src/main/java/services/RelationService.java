package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.RelationConverter;
import actions.views.RelationView;
import constants.JpaConst;
import models.Relation;
import models.validators.RelationValidator;

/**
 * 関係性テーブルの操作に関わる処理を行うクラス
 *
 */
public class RelationService extends ServiceBase {

    /**
     * 指定した従業員が作成した関係性データを、指定されたページ数の一覧画面に表示する分取得しRelationViewのリストで返却する
     * @param employee 従業員
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<RelationView> getMinePerPage(EmployeeView employee, int page) {

        List<Relation> relations = em.createNamedQuery(JpaConst.Q_REL_GET_ALL_MINE, Relation.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEEID, (EmployeeConverter.toModel(employee)).getId())
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .getResultList();
        return RelationConverter.toViewList(relations);
    }

    /**
     * 指定した従業員が作成した関係性データをすべて取得しRelationViewのリストで返却する
     * @param employee 従業員
     * @return 一覧画面に表示するデータのリスト
     */
    public List<RelationView> getMineAll(EmployeeView employee) {

        List<Relation> relations = em.createNamedQuery(JpaConst.Q_REL_GET_ALL_MINE, Relation.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEEID, (EmployeeConverter.toModel(employee)).getId())
                .getResultList();
        return RelationConverter.toViewList(relations);
    }


    /**
     * 指定した従業員が作成した関係性データの件数を取得し、返却する
     * @param employee
     * @return 関係性データの件数
     */
    public long countAllMine(EmployeeView employee) {

        long count = (long) em.createNamedQuery(JpaConst.Q_REL_COUNT_ALL_MINE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEEID, (EmployeeConverter.toModel(employee)).getId())
                .getSingleResult();

        return count;
    }


    /**
     * idを条件に取得したデータをRelationViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public RelationView findOne(int id) {
        return RelationConverter.toView(findOneInternal(id));
    }


    /**
     * 関係性情報の登録内容を元にデータを1件作成し、関係性テーブルに登録する
     * @param rv 関係性情報の登録内容
     */
    public List<String> create(RelationView rv, RelationService service, EmployeeView ev) {
        List<String> errors = RelationValidator.validate(rv, service, ev);
        if (errors.size() == 0) {
            LocalDateTime ldt = LocalDateTime.now();
            rv.setCreatedAt(ldt);
            rv.setUpdatedAt(ldt);
            createInternal(rv);
        }
        // バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }


    /**
     * アップデートは後から作成
     */


    /**
     * idを条件にデータを1件取得する
     * @param id
     * @return 取得データのインスタンス
     */
    private Relation findOneInternal(int id) {
        return em.find(Relation.class, id);
    }


    /**
     * フォロワーデータを1件登録する
     * @param fv フォロワーデータ
     */
    private void createInternal(RelationView rv) {

        em.getTransaction().begin();
        em.persist(RelationConverter.toModel(rv));
        em.getTransaction().commit();
    }
}
