package com.bufan.ssmredis.util;

import java.util.List;

public class PageUtil {

    private int pageNum;            //当前页
    private int totalCount;         //总的记录数
    private int pageSize;           //每页记录数
    private int pageTotal;          //总页数
    private List<?> list;           //集合
    private int startRowNum;        //当前页的开始记录编号
    private int endRowNum;          //当前页的最后记录编号
    private int first = 1;          //首页
    private int previous;           //上一页
    private int next;               //下一页
    private int last;               //末页
    private int navStartNum;        //下一页的开始记录编号
    private int navEndNum;          //下一页的最后记录编号
    private int navPageSize = 10;

    public PageUtil() {
        super();
    }

    public PageUtil(int page, int totalCount, int pageSize) {
        this.totalCount = totalCount;
        this.pageTotal = (int) (Math.ceil(this.totalCount * 1.0 / pageSize));
        this.pageNum = (page == 0) ? 1 : page;
        this.pageNum = Math.max(pageNum, 1);
        this.pageNum = Math.min(pageNum, pageTotal);
        this.pageSize = pageSize;
        this.startRowNum = (pageNum - 1) * pageSize + 1;
        this.endRowNum = startRowNum + pageSize - 1;
        this.previous = (pageNum == 1) ? 1 : (pageNum - 1);

        this.next = (pageNum == pageTotal) ? pageTotal : (pageNum + 1);
        this.last = pageTotal;

        this.navStartNum = (pageNum - navPageSize / 2 <= 0 ? 1 : pageNum - navPageSize / 2 + 1);
        this.navEndNum = Math.min(navStartNum + navPageSize - 1, last);
        if (navEndNum - navStartNum < navPageSize - 1) {
            navStartNum = Math.max(navEndNum - navPageSize + 1, 1);
        }
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public int getStartRowNum() {
        return startRowNum;
    }

    public void setStartRowNum(int startRowNum) {
        this.startRowNum = startRowNum;
    }

    public int getEndRowNum() {
        return endRowNum;
    }

    public void setEndRowNum(int endRowNum) {
        this.endRowNum = endRowNum;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getPrevious() {
        return previous;
    }

    public void setPrevious(int previous) {
        this.previous = previous;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public int getNavStartNum() {
        return navStartNum;
    }

    public void setNavStartNum(int navStartNum) {
        this.navStartNum = navStartNum;
    }

    public int getNavEndNum() {
        return navEndNum;
    }

    public void setNavEndNum(int navEndNum) {
        this.navEndNum = navEndNum;
    }

    public int getNavPageSize() {
        return navPageSize;
    }

    public void setNavPageSize(int navPageSize) {
        this.navPageSize = navPageSize;
    }
}
