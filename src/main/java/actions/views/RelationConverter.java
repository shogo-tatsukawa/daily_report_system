package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Relation;

/**
 * フォロワーデータのDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class RelationConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param rv RelationViewのインスタンス
     * @return Relationのインスタンス
     */
    public static Relation toModel(RelationView rv) {
        return new Relation(
                rv.getId(),
                EmployeeConverter.toModel(rv.getFollower()),
                EmployeeConverter.toModel(rv.getFollowed()),
                rv.getCreatedAt(),
                rv.getUpdatedAt());
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param r Relationのインスタンス
     * @return RelationViewのインスタンス
     */
    public static RelationView toView(Relation r) {

        if (r == null) {
            return null;
        }
        return new RelationView(
                r.getId(),
                EmployeeConverter.toView(r.getFollower()),
                EmployeeConverter.toView(r.getFollowed()),
                r.getCreatedAt(),
                r.getUpdatedAt());
    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデル
     * @return Viewモデルのリスト
     */
    public static List<RelationView> toViewList(List<Relation> list) {
        List<RelationView> rvs = new ArrayList<>();

        for (Relation r : list) {
            rvs.add(toView(r));
        }

        return rvs;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param r DTOモデル（コピー先）
     * @return rv Viewモデル（コピー元）
     */
    public static void copyViewToModel(Relation r, RelationView rv) {
        r.setId(rv.getId());
        r.setFollower(EmployeeConverter.toModel(rv.getFollower()));
        r.setFollowed(EmployeeConverter.toModel(rv.getFollowed()));
        r.setCreatedAt(rv.getCreatedAt());
        r.setUpdatedAt(rv.getUpdatedAt());
    }
}
