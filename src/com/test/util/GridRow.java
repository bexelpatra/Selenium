package com.test.util;

import org.openqa.selenium.JavascriptExecutor;
/*
 * 
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"OP_ACCT_CD","6006013" ));
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"OP_ACCT_NM","(����)�����Ļ���(��ħ�Ĵ�)" ));
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"EVDN_MNDR_YN","N" ));
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"PJT_MNDR_FG_YN","true" ));
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"CC_MNDR_FG_YN","true" ));
	
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"BG_CD","5000900000" ));
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"BG_NM","DT���ߺ���" ));
	
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"BGACCT_CD","5000001" ));
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"BGACCT_NM","(������)����" ));
	
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"CC_CD","6903" )); // ���� ���⼭ �Է����� �ʾƵ� �����ƺ��δ�.
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"CC_NM","����������" )); // ���� ���⼭ �Է����� �ʾƵ� �����ƺ��δ�.
	
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"PJT_CD","PJT202301013" ));
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"PJT_NM","����3��(KT/LGU+/SKT) ����Ͽ��������� ���� � (2023)" ));
	
	js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"NOTE_DC","��â�� / ��ħ�Ĵ�" ));
 * */
public class GridRow {
	
	private JavascriptExecutor js;
	// ����
	private String BG_CD;
	private String BG_NM;

	// ��
	private String CC_CD;
	private String CC_NM;

	// ������Ʈ
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
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"OP_ACCT_NM","(����)�����Ļ���(��ħ�Ĵ�)" ));
//		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"EVDN_MNDR_YN","N" ));
//		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"PJT_MNDR_FG_YN","true" ));
//		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"CC_MNDR_FG_YN","true" ));
		
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"BG_CD",BG_CD ));
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"BG_NM",BG_NM ));
		
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"BGACCT_CD","5000001" ));
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"BGACCT_NM","(������)����" ));
		
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"CC_CD",CC_CD ));
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"CC_NM",CC_NM));
		
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"PJT_CD",PJT_CD ));
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"PJT_NM",PJT_NM));
		
		js.executeScript(String.format("myGrid.setCellValue(%d,'%s', '%s')",i,"NOTE_DC","��ħ�Ĵ�("+PJT_NM+")"));
	}
	
}
