package com.meet.me.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.meet.me.domain.Board;
import com.meet.me.domain.MyHome;
import com.meet.me.domain.User;
import com.meet.me.service.MyHomeService;
import com.meet.me.service.UserService;


@Controller
public class MyhomeBoardController {

	@Autowired
	MyHomeService mhservice;

	@Autowired
	UserService userservice;

	//글쓰기페이지
	@GetMapping(value = "/BoardWrite.mh")
	public ModelAndView boardwrite(@RequestParam("id") String m_id, HttpSession session, ModelAndView mv, HttpServletRequest request) throws Exception {
		String user_id = (String) session.getAttribute("user_id1");
		int user_num = (int) session.getAttribute("user_num1");
		MyHome mh_info = mhservice.getinfo(m_id);
		System.out.println("m_id>>"+m_id);
		User userinfo = userservice.user_info(user_id);
		
		if (userinfo == null) {
			System.out.println("정보 수집 실패");
		} else {
			System.out.println("정보 수집 성공");
			
			mv.setViewName("myhome/mboard_write");
			mv.addObject("userinfo", userinfo);
			mv.addObject("mh_info", mh_info);
		}
		return mv;
	}
	
	//글쓰기
	@PostMapping("/BoardAddAction.mh")
	public String bbs_write_ok(HttpSession session, ModelAndView mv, Board board, HttpServletRequest request) throws Exception {

		String user_id = (String) session.getAttribute("user_id1");
		int user_num = (int) session.getAttribute("user_num1");
		User userinfo = userservice.user_info(user_id);
		//int result1 = mhservice.board_insert(board);
		
		MultipartFile uploadfile = board.getUploadfile();

		if (!uploadfile.isEmpty()) {
			String fileName = uploadfile.getOriginalFilename(); // 원래 파일 명
			board.setBOARD_ORIGINAL(fileName); // 원래 파일명 저장

			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int date = c.get(Calendar.DATE);

			//String saveFolder = "C:\\Users\\32426\\git\\m3\\meetme\\meetme2\\src\\main\\webapp\\resources\\upload\\";
			String saveFolder = request.getSession().getServletContext().getRealPath("resources") + "/upload/";
			String homedir = saveFolder + year + "-" + month + "-" + date;

			System.out.println(homedir);

			File path1 = new File(homedir);
			if (!(path1.exists())) { // 이 파일의 경로가 존재하는지 확인
				path1.mkdir(); // 없을 경우 경로 만들기
			}

			Random r = new Random();
			int random = r.nextInt(100000000);

			int index = fileName.lastIndexOf(".");
			System.out.println("index = " + index);
			String fileExtension = fileName.substring(index + 1); // 확장자만 따로 뻄
			System.out.println("fileExtension = " + fileExtension);
			String refileName = "bbs" + year + month + date + random + "." + fileExtension;
			System.out.println("refileName = " + refileName);
			String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
			System.out.println("fileDbName = " + fileDBName);
			uploadfile.transferTo(new File(saveFolder + fileDBName));

			// 바뀐 파일명으로 저장
			board.setBOARD_PHOTO(fileDBName);
		}
		mv.addObject("userinfo", userinfo);
		mhservice.insertBoard(board); // 저장 메서드 호출

		return "redirect:mboard.mh?id=" + user_id;
	}

	@RequestMapping(value = "/mboard.mh")
	public ModelAndView boardlist(@RequestParam("id") String u_id, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
			ModelAndView model) throws Exception {


		int limit = 3; // 한 화면에 보여줄 갯수

		// 총 리스트 수를 받아옵니다.
		int listcount = mhservice.getListCount(u_id); // 총 리스트 수

		int maxpage = (listcount + limit - 1) / limit;
		System.out.println("총 페이지수 : " + maxpage);

		int startpage = ((page - 1) / 10) * 10 + 1;
		System.out.println("현재 페이지에 보여줄 시작 페이지 수 = " + startpage);

		int endpage = startpage + 10 - 1;
		System.out.println("현재 페이지에 보여줄 마지막 페이지 수 =" + endpage);

		if (endpage > maxpage)
			endpage = maxpage;
		
		model.setViewName("myhome/mboard");
		List<Board> list = mhservice.boardlist(page, limit, u_id);

		System.out.println("page =" + page);
		System.out.println("limit =" + limit);
		
		model.addObject("page", page);
		model.addObject("maxpage", maxpage);
		model.addObject("startpage", startpage);
		model.addObject("endpage", endpage);
		model.addObject("limit", limit);
		model.addObject("boardlist", list);
		model.addObject("listcount", listcount);
		return model;
	}

//	@GetMapping("BoardReplyView.bo")
//	public ModelAndView BoardReplyView(int num, ModelAndView mv, HttpServletRequest request) {
//		Board board = boardservice.getDetail(num);
//		if (board == null) {
//			mv.setViewName("error/error");
//			mv.addObject("url", request.getRequestURL());
//			mv.addObject("message", "게시판 답변글 가져오기 실패");
//		} else {
//			mv.addObject("boarddata", board);
//			mv.setViewName("board/qna_board_reply");
//		}
//		return mv;
//	}
//
//	@PostMapping("BoardReplyAction.bo")
//	public ModelAndView BoardReplyAction(Board board, ModelAndView mv, HttpServletRequest request) throws Exception {
//		// boardservice.boardReplyUpdate(board);
//		int result = boardservice.boardReply(board);
//		if (result == 0) {
//			mv.setViewName("error/error");
//			mv.addObject("url", request.getRequestURL());
//			mv.addObject("message", "게시판 답변 처리실패");
//		} else {
//			mv.setViewName("redirect:BoardList.bo");
//		}
//		return mv;
//	}

	@GetMapping("BoardModifyView.mh")
	public ModelAndView BoardModifyView(int num, ModelAndView mv, HttpServletRequest request) throws Exception {
		Board board = mhservice.getDetail(num);
		// 글 내용 불러오기 실패한 경우입니다.
		if (board == null) {
			System.out.println("(수정)상세보기 실패");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "(수정)상세보기 실패");
			return mv;
		}
		System.out.println("(수정)상세보기 성공");

		// 수정 폼 페이지로 이동할 때 원문 글 내용을 보여주기 때문에 board 객체를
		// ModelAndView 객체에 저장합니다.
		mv.addObject("boarddata", board);
		// 글 수정 폼 페이지로 이동하기 위해 경로를 설정합니다.
		mv.setViewName("myhome/mboard_modify");
		return mv;
	}

	@PostMapping("BoardModifyAction.mh")
	public ModelAndView BoardModifyAction(@RequestParam("id") String m_id, Board board, String before_file, ModelAndView mv, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MultipartFile uploadfile = board.getUploadfile();
		String saveFolder = request.getSession().getServletContext().getRealPath("resources") + "/upload/";

		if (uploadfile != null && !uploadfile.isEmpty()) { // 파일 변경한 경우
			System.out.println("파일 변경한 경우");
			String fileName = uploadfile.getOriginalFilename(); // 원래 파일
			board.setBOARD_ORIGINAL(fileName);
			String fileDBName = fileDBName(fileName, saveFolder);

			// transferTo(File path):업로드한 파일을 매개변수의 경로에 저장합니다.
			uploadfile.transferTo(new File(saveFolder + fileDBName));

			// 바뀐 파일명으로 저장
			board.setBOARD_PHOTO(fileDBName);
		}

		int result = mhservice.boardModify(board);
		if (result == 0) {
			System.out.println("게시판 수정 실패");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "게시판 수정실패");
		} else {// 수정 성공의 경우
			System.out.println("게시판 수정 완료");
			
//			//추가
//			//수정 전에 파일이 있고 새로운 파일을 선택한 경우는 삭제할 목록을 테이블에 추가합니다.
//			if(!before_file.equals("")&&!before_file.contentEquals(board.getBOARD_PHOTO())) {
//				mhservice.insert_deleteFile(before_file);
//			}
			String url = "redirect:mboard.mh?id=" + m_id;

			// 수정한 글 내용을 보여주기 위해 글 내용 보기 페이지로 이동하기 위해 경로를 설정합니다.
			mv.setViewName(url);
		}
		return mv;
	}

//	@PostMapping("BoardDeleteAction.bo")
//	public String BoardDeleteAction(String BOARD_PASS, int num, HttpServletResponse response) throws Exception {
//		// 글 삭제 명령을 요청한 사용자가 글을 작성한 사용자인지 판단하기 위해
//		// 입력한 비밀번호와 저장된 비밀번호를 비교하여 일치하면 삭제합니다.
//
//		boolean usercheck = boardservice.isBoardWriter(num, BOARD_PASS);
//
//		// 비밀번호 일치하지 않는 경우
//		if (usercheck == false) {
//			response.setContentType("text/html;charset=utf-8");
//			PrintWriter out = response.getWriter();
//			out.println("<script>");
//			out.println("alert('비밀번호가 다릅니다.');");
//			out.println("history.back();");
//			out.println("</script>");
//			out.close();
//			return null;
//		}
//		// 비밀번호 일치하는 경우 삭제 처리 합니다.
//		int result = boardservice.boardDelete(num);
//
//		// 삭제 처리 실패한 경우
//		if (result == 0) {
//			System.out.println("게시판 삭제 실패");
//		}
//		// 삭제 처리 성공한 경우 - 글 목록 보기 요청을 전송하는 부분입니다.
//		System.out.println("게시판 삭제 성공");
//		response.setContentType("text/html;charset=utf-8");
//		PrintWriter out = response.getWriter();
//		out.println("<script>");
//		out.println("alert('삭제되었습니다.');");
//		out.println("location.href='BoardList.bo';");
//		out.println("</script>");
//		out.close();
//		return null;
//	}
//
//	@ResponseBody
//	@RequestMapping(value = "/BoardListAjax.bo")
//	public Map<String, Object> boardListAjax(
//			@RequestParam(value = "page", defaultValue = "1", required = false) int page,
//			@RequestParam(value = "limit", defaultValue = "10", required = false) int limit) throws Exception {
//
//		// 총 리스트 수를 받아옵니다.
//		int listcount = boardservice.getListCount(); // 총 리스트 수
//
//		int maxpage = (listcount + limit - 1) / limit;
//		System.out.println("총 페이지수 : " + maxpage);
//
//		int startpage = ((page - 1) / 10) * 10 + 1;
//		System.out.println("현재 페이지에 보여줄 시작 페이지 수 = " + startpage);
//
//		// endpage: 현재 페이지 그룹에서 보여줄 마지막 페이지수([10],[20],[30]) 등
//		int endpage = startpage + 10 - 1;
//		System.out.println("현재 페이지에 보여줄 마지막 페이지 수 =" + endpage);
//
//		if (endpage > maxpage)
//			endpage = maxpage;
//		List<Board> boardlist = boardservice.boardlist(page, limit);
//
//		// BoardAjax 이용하기
//		/*
//		 * BoardAjax ba = new BoardAjax(); ba.setPage(page); ba.setMaxpage(maxpage);
//		 * ba.setStartpage(startpage); ba.setEndpage(endpage);
//		 * ba.setListcount(listcount); ba.setBoardlist(boardlist); ba.setLimit(limit);
//		 */
//		// Map 이용하기
//		System.out.println("map 이용하기");
//		Map<String, Object> ba = new HashMap<String, Object>();
//		ba.put("page", page);
//		ba.put("maxpage", maxpage);
//		ba.put("startpage", startpage);
//		ba.put("endpage", endpage);
//		ba.put("listcount", listcount);
//		ba.put("boardlist", boardlist);
//		ba.put("limit", limit);
//		return ba;
//	}
//
//	@GetMapping("BoardFileDown.bo")
//	public void BoardFileDown(String filename, HttpServletRequest request, String original,
//			HttpServletResponse response) throws Exception {
//		
//		// 서블릿의 실행 환경 정보를 담고있는 객체를 리턴한다.
//				ServletContext context = request.getSession().getServletContext();
//				
//		//1.
//		//String savePath = "resources/upload";
//		//String sDownloadPath = context.getRealPath(savePath);
//		//String sFilePath = sDownloadPath + "\\" + filename;
//		//"\" 추가하기 위해 "\\" 사용합니다.
//		//String sFilePath = sDownloadPath + "/" + filename;
//
//		//2.
//		String sFilePath = saveFolder +"/" +filename;
//		System.out.println(sFilePath);
//		
//		byte b[] = new byte[4096];
//
//		// sFilePath에 있는 파일의 MimeType을 구해온다.
//		String sMimeType = context.getMimeType(sFilePath);
//		System.out.println("sMimeType>>>" + sMimeType);
//
//		if (sMimeType == null)
//			sMimeType = "application/octet-stream";
//
//		response.setContentType(sMimeType);
//
//		// 한글 깨짐 방지
//		String sEncoding = new String(original.getBytes("utf-8"), "ISO-8859-1");
//		System.out.println(sEncoding);
//
//		/*
//		 * Content-Disposition: attatchment:브라우저는 해당 Content를 처리하지 않고 다운로드 한다.
//		 */
//		response.setHeader("Content-Disposition", "attatchment; filename=" + sEncoding);
//
//		// 프로젝트 속성 - 자바버전 1.8로 변경
//		try (
//				// 웹 브라우저로의 출력 스트림 생성한다.
//				BufferedOutputStream out2 = new BufferedOutputStream(response.getOutputStream());
//				// sFilePath로 지정한 파일에 대한 입력 스트림을 생성한다.
//				BufferedInputStream in = new BufferedInputStream(new FileInputStream(sFilePath));) {
//			int numRead;
//			// read(b, 0, b.length):바이트 배열 b의 0번부터 b.length
//			// 크기만큼 읽어온다
//			while ((numRead = in.read(b, 0, b.length)) != -1) { // 읽을 데이터가 존재
//				// 바이트 배열 b의 0번부터 numRead크기만큼 브라우저로 출력
//				out2.write(b, 0, numRead);
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	private String fileDBName(String fileName, String saveFolder) {
		// 새로운 폴더 이름 : 오늘 년+월+일
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR); // 오늘 년도 구합니다.
		int month = c.get(Calendar.MONTH) + 1; // 오늘 월 구합니다.
		int date = c.get(Calendar.DATE); // 오늘 일 구합니다.

		String homedir = saveFolder + year + "-" + month + "-" + date;
		System.out.println(homedir);
		File path1 = new File(homedir);
		if (!(path1.exists())) { // 이 파일의 경로가 존재하는지 확인
			path1.mkdir(); // 없을 경우 새로운 폴더를 생성
		}

		// 난수를 구합니다. 사용자가 올린 파일의 이름이 중복되면 안되니까
		Random r = new Random();
		int random = r.nextInt(100000000);

		/* 확장자 구하기 시작 //// 원래 파일의 형식이 .png / .jpg / 같은 형식일거니까 */
		int index = fileName.lastIndexOf(".");
		/*
		 * 문자열에서 특정 문자열의 위치 값을 반환한다. indexOf가 처음 발견되는 문자열에 대한 index를 반환하는 반면,
		 * lastIndexOf는 마지막으로 발견되는 문자열의 index를 반환합니다. (파일명에 점이 여러개 있을 경우 맨 마지막에 발견되는
		 * 문자열의 위치를 리턴합니다.)
		 */

		System.out.println("index = " + index);

		String fileExtension = fileName.substring(index + 1); // 확장자만 따로 뻄
		System.out.println("fileExtension = " + fileExtension);
		/* 확장자 구하기 끝 */

		// 새로운 파일명을 저장
		String refileName = "bbs" + year + month + date + random + "." + fileExtension;
		System.out.println("refileName = " + refileName);

		// 오라클 디비에 저장될 파일명
		String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
		System.out.println("fileDbName = " + fileDBName);

		return fileDBName;
	}
}
