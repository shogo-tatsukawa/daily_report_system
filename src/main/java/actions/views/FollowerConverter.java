package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Follower;

/**
 * フォロワーデータのDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class FollowerConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param fv FollwerViewのインスタンス
     * @return Followerのインスタンス
     */
    public static Follower toModel(FollowerView fv) {
        return new Follower(
                fv.getId(),
                EmployeeConverter.toModel(fv.getEmployee()),
                ReportConverter.toModel(fv.getFollower()),
                fv.getCreatedAt(),
                fv.getUpdatedAt());
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param f Followerのインスタンス
     * @return FollowerViewのインスタンス
     */
    public static FollowerView toView(Follower f) {

        if (f == null) {
            return null;
        }

        return new FollowerView(
                f.getId(),
                EmployeeConverter.toView(f.getEmployee()),
                ReportConverter.toView(f.getFollower()),
                f.getCreatedAt(),
                f.getUpdatedAt());
    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデル
     * @return Viewモデルのリスト
     */
    public static List<FollowerView> toViewList(List<Follower> list) {
        List<FollowerView> fvs = new ArrayList<>();

        for (Follower f : list) {
            fvs.add(toView(f));
        }

        return fvs;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param f DTOモデル（コピー先）
     * @return fv Viewモデル（コピー元）
     */
    public static void copyViewToModel(Follower f, FollowerView fv) {
        f.setId(fv.getId());
        f.setEmployee(EmployeeConverter.toModel(fv.getEmployee()));
        f.setFollower(ReportConverter.toModel(fv.getFollower()));
        f.setCreatedAt(fv.getCreatedAt());
        f.setUpdatedAt(fv.getUpdatedAt());
    }
}
