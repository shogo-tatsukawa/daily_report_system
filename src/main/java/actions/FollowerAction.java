package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.FollowerView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import services.FollowerService;

/**
 * フォロワーに関する処理を行うActionクラス
 */
public class FollowerAction extends ActionBase {

    private FollowerService service;


    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new FollowerService();

        // メソッドを実行
        invoke();
        service.close();
    }


    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException {

        // セッションからログイン中の従業員情報を取得
        EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        //指定されたページ数の一覧画面に表示するフォロワーデータを取得
        int page = getPage();
        List<FollowerView> followers = service.getMinePerPage(loginEmployee, page);

        // ログイン中の従業員が作成したフォロワーデータの件数を取得
        long myFollowersCount = service.countAllMine(loginEmployee);

        putRequestScope(AttributeConst.REPORTS, followers);  // 取得したフォロワーデータ
        putRequestScope(AttributeConst.REP_COUNT, myFollowersCount);  // ログイン中の従業員が作成したフォロワーの数
        putRequestScope(AttributeConst.PAGE, page);  // ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE);  // 1ページに表示するレコードの数

        // セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH)
            ;
        }

        // 一覧画面を表示
        forward(ForwardConst.FW_FLW_INDEX);
    }

}
