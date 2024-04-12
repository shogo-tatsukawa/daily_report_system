package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.FollowerView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import services.FollowerService;
import services.ReportService;

/**
 * フォロワーに関する処理を行うActionクラス
 */
public class FollowerAction extends ActionBase {

    private FollowerService service;
    private ReportService service_rep;


    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new FollowerService();
        service_rep = new ReportService();

        // メソッドを実行
        invoke();
        service.close();
        service_rep.close();
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


    /**
     * 新規登録画面を表示する
     */
    public void entryNew() throws ServletException, IOException {

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン

        // idを条件に日報データを取得する
        ReportView rv = service_rep.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

        if (rv == null) {
            // 該当の日報データが存在しない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {
            //フォロワー情報の空インスタンスに、フォロワーのIDを設定する
            FollowerView fv = new FollowerView();
            fv.setFollower(rv);
            putRequestScope(AttributeConst.FOLLOWER, fv);  // 取得した日報データ

            // 新規登録画面を表示
            forward(ForwardConst.FW_FLW_NEW);
        }
    }

}
