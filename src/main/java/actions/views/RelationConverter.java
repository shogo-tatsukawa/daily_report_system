package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Relation;

/**
 * 関係性データのDTOモデル⇔Viewモデルの変換を行うクラス
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
                rv.getFollower_id(),
                EmployeeConverter.toModel(rv.getFollowed()),
                //rv.getFollowed_id(),
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
                r.getFollower_id(),
                EmployeeConverter.toView(r.getFollowed()),
                //r.getFollowed_id(),
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
        r.setFollower_id(rv.getFollower_id());
        r.setFollowed(EmployeeConverter.toModel(rv.getFollowed()));
        //r.setFollowed_id(rv.getFollowed_id());
        r.setCreatedAt(rv.getCreatedAt());
        r.setUpdatedAt(rv.getUpdatedAt());
    }
}
