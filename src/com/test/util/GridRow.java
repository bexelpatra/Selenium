package com.test.util;

import org.openqa.selenium.JavascriptExecutor;
/*
 * 
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"OP_ACCT_CD","6006013" ));
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"OP_ACCT_NM","(원가)복리후생비(아침식대)" ));
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"EVDN_MNDR_YN","N" ));
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"PJT_MNDR_FG_YN","true" ));
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"CC_MNDR_FG_YN","true" ));
	
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"BG_CD","5000900000" ));
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"BG_NM","DT개발본부" ));
	
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"BGACCT_CD","5000001" ));
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"BGACCT_NM","(미통제)지출" ));
	
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"CC_CD","6903" )); // 굳이 여기서 입력하지 않아도 괜찮아보인다.
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"CC_NM","인증서비스팀" )); // 굳이 여기서 입력하지 않아도 괜찮아보인다.
	
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"PJT_CD","PJT202301013" ));
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"PJT_NM","이통3사(KT/LGU+/SKT) 모바일운전면허증 서비스 운영 (2023)" ));
	
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"NOTE_DC","북창동 / 아침식대" ));
 * */
public class GridRow {
	
	private JavascriptExecutor js;
	// 본부
	private String BG_CD;
	private String BG_NM;

	// 팀
	private String CC_CD;
	private String CC_NM;

	// 프로젝트
	private String PJT_CD;
	private String PJT_NM;

	
	
	
	public GridRow(JavascriptExecutor js, String bG_CD, String bG_NM, String cC_CD, String cC_NM, String pJT_CD,
			String pJT_NM) {
		super();
		this.js = js;
		BG_CD = bG_CD;
		BG_NM = bG_NM;
		CC_CD = cC_CD;
		CC_NM = cC_NM;
		PJT_CD = pJT_CD;
		PJT_NM = pJT_NM;
	}

	public void execute(int i,String OP_ACCT_CD,String OP_ACCT_NM ,String BGACCT_CD, String BGACCT_NM ,String NOTE_DC) {
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"OP_ACCT_CD",OP_ACCT_CD));
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"OP_ACCT_NM", OP_ACCT_NM));
//		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"EVDN_MNDR_YN","N" ));
//		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"PJT_MNDR_FG_YN","true" ));
//		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"CC_MNDR_FG_YN","true" ));
		
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"BG_CD",BG_CD ));
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"BG_NM",BG_NM ));
		
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"BGACCT_CD",BGACCT_CD ));
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"BGACCT_NM",BGACCT_NM ));
		
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"CC_CD",CC_CD ));
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"CC_NM",CC_NM));
		
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"PJT_CD",PJT_CD ));
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"PJT_NM",PJT_NM));
		
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"NOTE_DC",NOTE_DC));
	}

	public void breakfast(int i) {
		
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"OP_ACCT_CD","6006013" ));
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"OP_ACCT_NM","(원가)복리후생비(아침식대)" ));
//		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"EVDN_MNDR_YN","N" ));
//		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"PJT_MNDR_FG_YN","true" ));
//		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"CC_MNDR_FG_YN","true" ));
		
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"BG_CD",BG_CD ));
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"BG_NM",BG_NM ));
		
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"BGACCT_CD","5000001" ));
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"BGACCT_NM","(미통제)지출" ));
		
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"CC_CD",CC_CD ));
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"CC_NM",CC_NM));
		
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"PJT_CD",PJT_CD ));
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"PJT_NM",PJT_NM));
		
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"NOTE_DC","아침식대("+PJT_NM+")"));
	}
	
}
