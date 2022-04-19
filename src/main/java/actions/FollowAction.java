package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.FollowView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import models.Employee;
import models.Follow;
import services.FollowService;

/**
 * 日報に関する処理を行うActionクラス
 *
 */
public class FollowAction extends ActionBase{
    private FollowService service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException{
        service = new FollowService();

        //メソッドを実行
        invoke();
        service.close();
    }

    /**
     * フォロー一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void fol_index() throws ServletException, IOException{
        //指定されたページ数の一覧画面に表示するフォローデータを取得
        int page = getPage();

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        //Employee型へキャスト
        Employee evEmployee = EmployeeConverter.toModel(ev);

       //フォローしている人全員を取得
        List<FollowView> followings = service.getAllFollowigPerPage(page, evEmployee);

        //フォローしている件数を取得
        long followingsCount = service.countAllFollowing(evEmployee);


        putRequestScope(AttributeConst.FOLLOWINGS, followings); //取得した フォローデータ
        putRequestScope(AttributeConst.FOL_COUNT, followingsCount); //全てのフォロー件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数
        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_FOL_INDEX);
    }

    /**
     * フォロワー一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void foler_index() throws ServletException, IOException{
        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        //Employee型へキャスト
        Employee evEmployee = EmployeeConverter.toModel(ev);

        //指定されたページ数の一覧画面に表示するフォローデータを取得
        int page = getPage();
        List<FollowView> followers = service.getAllFollowerPerPage(page, evEmployee);

        //全日報データの件数を取得
        long followersCount = service.countAllFollower(evEmployee);

        putRequestScope(AttributeConst.FOLLOWERS, followers); //取得した フォローデータ
        putRequestScope(AttributeConst.FOLER_COUNT, followersCount); //全ての日報データの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数
        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_FOLER_INDEX);
    }

    /**
     * フォロー新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {
        //CSRF対策 tokenのチェック
        if(checkToken()) {

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = getSessionScope(AttributeConst.LOGIN_EMP);

        //リクエストスコープのidからフォローする従業員情報を取得
        EmployeeView evf = service.findOneEmployee(toNumber(getRequestParam(AttributeConst.FOLLOWER)));

        //パラメータの値をもとにフォロー情報のインスタンスを作成する
        //★追記--Employee型へキャスト
        Employee evEmployee = EmployeeConverter.toModel(ev);
        Employee efEmployee = EmployeeConverter.toModel(evf);

        FollowView fv = new FollowView(
                null,
                evEmployee,
                efEmployee);

        //日報情報登録
        service.create(fv);

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
//        putRequestScope(AttributeConst.FOLLOW, f);//フォロワー

      //一覧画面にリダイレクト
        redirect(ForwardConst.ACT_TOP, ForwardConst.CMD_INDEX);
        }

    }

    /**
     * フォロー解除を行う
     * @throws ServletException
     * @throws IOException
     */
    public void destroy() throws ServletException, IOException {
        //CSRF対策 tokenのチェック
        if(checkToken()) {
            //セッションからログイン中の従業員情報を取得
            EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            //リクエストスコープのidからフォローを解除する従業員情報を取得
            EmployeeView evf = service.findOneEmployee(toNumber(getRequestParam(AttributeConst.FOLLOWER)));

            if (evf == null && ev == null) {
                //データが取得できなかった場合はエラー画面を表示
                forward(ForwardConst.FW_ERR_UNKNOWN);
                return;

            } else {
                //★追記--Employee型へキャスト
                Employee evEmployee = EmployeeConverter.toModel(ev);
                Employee efEmployee = EmployeeConverter.toModel(evf);

                Follow f = service.findFollow(evEmployee, efEmployee);

                if (f == null) {
                    //データが取得できなかった場合はエラー画面を表示
                    forward(ForwardConst.FW_ERR_UNKNOWN);
                    return;

                } else {
                    service.destroy(f);
                    //トップページにリダイレクト
                    redirect(ForwardConst.ACT_TOP, ForwardConst.CMD_INDEX);
                }
            }
       }
    }
}
