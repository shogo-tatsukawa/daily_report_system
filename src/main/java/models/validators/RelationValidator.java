package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.EmployeeView;
import actions.views.RelationView;
import constants.MessageConst;
import services.RelationService;
/**
 *
 * 関係性インスタンスに設定されている値のバリデーションを行うクラス
 *
 */
public class RelationValidator {

    /**
     * 関係性インスタンスの各項目についてバリデーションを行う
     * @param rv 関係性インスタンス
     * @param service 関係性サービスインスタンス
     * @param ev ログインしている従業員の従業員インスタンス
     * @return エラーのリスト
     */
    public static List<String> validate(RelationView rv, RelationService service, EmployeeView loginEmployee) {
        List<String> errors = new ArrayList<String>();

        // フォロワーがすでに登録されていないかのチェック
        String followedError = validateFollowed(rv.getFollowed(), service, loginEmployee);
        if (!followedError.equals("")) {
            errors.add(followedError);
        }

        return errors;
    }


    /**
     * フォロワーがすでに登録されているかをチェックし、すでに登録されていればエラーメッセージを返却
     * @param followed(EmployeeViewオブジェクト) フォロワーの従業員情報
     * @return エラーメッセージ
     */
    private static String validateFollowed(EmployeeView ev, RelationService service, EmployeeView loginEmployee) {
        // フォローしている従業員のリストを取得
        List<RelationView> followedList = service.getMineAll(loginEmployee);

        for (RelationView rv : followedList) {
            if (rv.getFollowed().getId() == ev.getId()) {
                return MessageConst.E_REL_FLD_EXIST.getMessage();
            }
        }

        // 入力値が正しい場合は空文字を返却
        return "";
    }
}
