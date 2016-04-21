/**
 * Copyright (C) 2011 Headvances Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * This project aim to build a set of library/data to process 
 * the Vietnamese language and analyze the web data
 **/
package org.vsw.nlp.ml.crf.ws;

import java.io.PrintStream;

import org.junit.Test;
import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.io.DefaultDocumentReader;
import org.vsw.nlp.doc.io.DocumentReader;
import org.vsw.nlp.token.TabularTokenPrinter;
import org.vsw.util.ConsoleUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class CRFWordSegmenterUnitTest {
	@Test
	public void test() throws Exception {
		DocumentReader reader = new DefaultDocumentReader(NLPResource.getInstance()) ;
		String text1 = "chủ tịch nước Nguyễn Tấn Dũng đến thăm và làm việc tại Hà Nội." ;
		String text2 = "- TS Trần Thế Trung, Viện trưởng Viện nghiên cứu công nghệ FPT: Viện đang có một số dự án xoay quanh công nghệ thông tin, ngoài ra có cả dự án về Công nghệ vũ trụ, Công nghệ sinh học.";
		String text3 = "Lãnh đạo ĐH FPT và sinh viên Trần Hải Đăng tham gia buổi tư vấn trực tuyến. Ảnh: sinh viên." ;
		String text4 = "Phiến quân tại Libya đã đẩy lùi kế hoạch tái chiến thị trấn dầu lửa Brega ở phía Đông từ các nhóm lính trung thành với Đại tá Muammar Gaddafi." ;
		String text5 = "Nguyễn Hoàng mâu thuẫn với ai thời hậu lê" ;
		test(reader, text5) ;
	}
	
	private void test(DocumentReader reader, String text) throws Exception {
		PrintStream out = ConsoleUtil.getUTF8SuportOutput() ;
		TabularTokenPrinter printer = new TabularTokenPrinter();
		printer.print(out, reader.read(text).getTokens()) ;
	}
	
}
