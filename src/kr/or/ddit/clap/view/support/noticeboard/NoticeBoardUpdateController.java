package kr.or.ddit.clap.view.support.noticeboard;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.noticeboard.INoticeBoardService;
import kr.or.ddit.clap.vo.support.NoticeBoardVO;

public class NoticeBoardUpdateController implements Initializable {
	
	private Registry reg;
	private INoticeBoardService ins;
	public static String NoticeNo;
	public NoticeBoardVO nVO = new NoticeBoardVO();
	
	@FXML
	AnchorPane I_main;
	@FXML
	JFXTextField Text_ntcTitle;
	@FXML
	JFXTextArea Text_ntcContent;
	@FXML
	JFXButton btn_upd;
	@FXML
	Label lb_id;
	
	// 전 화면에 있는 데이터를 그대로 가져와  세팅해주는 메서드
	public void initData(NoticeBoardVO nVO) {
		System.out.println("Notice-initData");
		this.nVO= nVO;
		Text_ntcTitle.setText(nVO.getNotice_title());
		Text_ntcContent.setText(nVO.getNotice_content());
		lb_id.setText(LoginSession.session.getMem_id());
		
	}
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ins = (INoticeBoardService) reg.lookup("notice");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		lb_id.setText(LoginSession.session.getMem_id()); //현재 로그인한 사용자
		
		btn_upd.setOnAction(e -> {
			
			if(Text_ntcTitle.getText().isEmpty() || Text_ntcContent.getText().isEmpty()) {
				errMsg("작업오류" , "빈 항목이 있습니다.");
				return;
				
			} else {
				nVO.setNotice_title(Text_ntcTitle.getText());
				nVO.setNotice_content(Text_ntcContent.getText());
				nVO.setMem_id(LoginSession.session.getMem_id());
				nVO.setNotice_view_cnt(nVO.getNotice_view_cnt());
				nVO.setNotice_no(NoticeNo);
				
				try {
					int flag=ins.updateNotice(nVO);
				} catch (RemoteException ee) {
					
					ee.printStackTrace();
				}
				
				infoMsg("등록 완료", "문의사항 - 글 작성이 완료되었습니다.");
				
				Parent root1;
				try {
					root1 = FXMLLoader.load(getClass().getResource("NoticeBoardDetailContent.fxml"));
					I_main.getChildren().removeAll();
					I_main.getChildren().setAll(root1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			
		});
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	public void errMsg(String headerText, String msg) {
		Alert errAlert = new Alert(AlertType.ERROR);
		errAlert.setTitle("오류");
		errAlert.setHeaderText(headerText);
		errAlert.setContentText(msg);
		errAlert.showAndWait();
	}
	
	public void infoMsg(String headerText, String msg) {
		Alert infoAlert = new Alert(AlertType.INFORMATION);
		infoAlert.setTitle("정보 확인");
		infoAlert.setHeaderText(headerText);
		infoAlert.setContentText(msg);
		infoAlert.showAndWait();
	}
	
}
