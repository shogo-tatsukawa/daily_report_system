package actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.RelationView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.EmployeeService;
import services.RelationService;
import services.ReportService;

/**
 * フォロワーに関する処理を行うActionクラス
 */
public class RelationAction extends ActionBase {

    private RelationService service;
    private EmployeeService service_emp;
    private ReportService service_rep;


    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new RelationService();
        service_emp = new EmployeeService();
        service_rep = new ReportService();

        // メソッドを実行
        invoke();
        service.close();
        service_emp.close();
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
        List<RelationView> relations = service.getMinePerPage(loginEmployee, page);

        // ログイン中の従業員が作成したフォロワーデータの件数を取得
        long myRelationsCount = service.countAllMine(loginEmployee);

        putRequestScope(AttributeConst.RELATIONS, relations);  // 取得したフォロワーデータ
        putRequestScope(AttributeConst.REL_COUNT, myRelationsCount);  // ログイン中の従業員が作成したフォロワーの数
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
        forward(ForwardConst.FW_REL_INDEX);
    }

    /**
     * フォロワーしている従業員の日報を一覧表示する
     * @throws ServletException
     * @throws IOException
     */
    public void indexFollowed() throws ServletException, IOException {

        // セッションからログイン中の従業員情報を取得
        EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        // フォローしている従業員のリストを取得
        List<RelationView> followedList = service.getMineAll(loginEmployee);


        // IDリストに変換
        List<Integer> followedId = new ArrayList<>();

        for (RelationView rv : followedList) {
            followedId.add(rv.getFollowed().getId());
        }

        //指定されたページ数の一覧画面に表示する日報データを取得
        int page = getPage();
        List<ReportView> reports = service_rep.getSelectPerPage(followedId, page);

        // フォローしている従業員が作成した日報データの件数を取得
        long reportsCount = service_rep.countAllSelect(followedId);

        putRequestScope(AttributeConst.REPORTS, reports);  // 取得したフォロワーデータ
        putRequestScope(AttributeConst.REP_COUNT, reportsCount);  // ログイン中の従業員が作成したフォロワーの数
        putRequestScope(AttributeConst.PAGE, page);  // ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE);  // 1ページに表示するレコードの数


        putRequestScope(AttributeConst.RELATIONS, followedList);  // 取得したフォロワーデータ

        // セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH)
            ;
        }

        // 一覧画面を表示
        forward(ForwardConst.FW_REP_INDEX);
        //forward(ForwardConst.FW_REL_TEST);
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
            //関係情報の空インスタンスにfollowedのIDを設定する
            RelationView relv = new RelationView();
            relv.setFollowed(rv.getEmployee());
            putRequestScope(AttributeConst.RELATION, relv);  // 取得したフォロワーデータ
            //putRequestScope(AttributeConst.REPORT, rv);  // 取得したReportデータ
            // putRequestScope(AttributeConst.REP_ID, toNumber(getRequestParam(AttributeConst.REP_ID)));  // レポートID

            // 新規登録画面を表示
            forward(ForwardConst.FW_REL_NEW);
        }
    }


    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //セッションからログイン中の従業員情報を取得
            EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            // リクエストスコープからフォローされた人が作成したレポートのID情報を取得
            // idを条件に日報データを取得する
            EmployeeView followedEmployee = service_emp.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));
            // idを条件に日報データを取得する
            //Integer followed_id = toNumber(getRequestParam(AttributeConst.EMP_ID));

            //if (followedEmployee == null) {
            if (loginEmployee == null) {
                // 該当の従業員データが存在しない場合はエラー画面を表示
                forward(ForwardConst.FW_ERR_UNKNOWN);

            } else {
                // パラメータの値を元にフォロワー情報のインスタンスを作成する
                RelationView relv = new RelationView(
                        null,
                        loginEmployee.getId(),  // ログインしている従業員をフォロワー作成者として登録する
                        //followed_id,
                        followedEmployee,
                        null,
                        null);

                // フォロワー登録
                List<String> errors = service.create(relv, service, loginEmployee);

                if (errors.size() > 0) {
                    // 登録中にエラーがあった場合

                    // エラーメッセージを結合
                    String errorMessage = "";
                    for (String error : errors) {
                        errorMessage = errorMessage + error + "\n";
                    }
                    // セッションに登録エラーのフラッシュメッセージを設定
                    putSessionScope(AttributeConst.FLUSH, errorMessage);

                    // 一覧画面にリダイレクト
                    redirect(ForwardConst.ACT_REL, ForwardConst.CMD_INDEX);
                } else {
                    // 登録中にエラーがなかった場合

                    //セッションに登録完了のフラッシュメッセージを設定
                    putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                    //一覧画面にリダイレクト
                    redirect(ForwardConst.ACT_REL, ForwardConst.CMD_INDEX);
                }

            }

        }
    }

    /**
     * 詳細画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException {

        // 従業員IDを元に従業員情報を取得する
        EmployeeView ev = service_emp.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));

        // フォロワー中の従業員が作成した日報データを、指定されたページ数の一覧画面に表示する分取得する
        int page = getPage();
        List<ReportView> reports = service_rep.getMinePerPage(ev, page);

        //ログイン中の従業員が作成した日報データの件数を取得
        long myReportsCount = service_rep.countAllMine(ev);

        putRequestScope(AttributeConst.REPORTS, reports); //取得した日報データ
        putRequestScope(AttributeConst.REP_COUNT, myReportsCount); //ログイン中の従業員が作成した日報の数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_REP_INDEX);
    }

}
