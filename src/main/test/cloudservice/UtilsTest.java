package cloudservice;

import com.dcits.cloud.model.Page;

public class UtilsTest {

	public static void main(String args[]){
		Page pages = new Page();
		pages.setCurrentPage(4L);
		System.out.println(pages.getNextPage());
		System.out.println(pages.getStartIndex());
		System.out.println(pages.getHasNextPage());
	}
}
