package com.planet.wondering.chemi.model.category;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 7. 5..
 */

public class CategoryPart {

    private int mCategoryGroupId;
    private int mCategoryPartId;
    private int mNameResId;
    private String mName;
    private int mImageResId;
    private ArrayList<CategoryPiece> mCategoryPieces;

    public CategoryPart(int categoryGroupId, int categoryPartId, String name, int imageResId) {
        mCategoryGroupId = categoryGroupId;
        mCategoryPartId = categoryPartId;
        mName = name;
        mImageResId = imageResId;
        mCategoryPieces = new ArrayList<>();
        setCategoryPart(mCategoryGroupId, mCategoryPartId);
    }

    public CategoryPart(int nameResId, int imageResId) {
        mNameResId = nameResId;
        mImageResId = imageResId;
    }

    private void setCategoryPart(int categoryGroupId, int categoryPartId) {
        switch (categoryGroupId) {
            case 0:
                switch (categoryPartId) {
                    case 0:
                        mCategoryPieces.add(new CategoryPiece("이른둥이 남여공용", 13));
                        mCategoryPieces.add(new CategoryPiece("신생아 남여공용", 14));

                        mCategoryPieces.add(new CategoryPiece("소형"));
                        ArrayList<CategoryPiece> babyNappySmallCategoryPieces = new ArrayList<>();
                        babyNappySmallCategoryPieces.add(new CategoryPiece("남아용", 15));
                        babyNappySmallCategoryPieces.add(new CategoryPiece("여아용", 16));
                        babyNappySmallCategoryPieces.add(new CategoryPiece("남여공용", 17));
                        mCategoryPieces.add(new CategoryPiece(babyNappySmallCategoryPieces));

                        mCategoryPieces.add(new CategoryPiece("중형"));
                        ArrayList<CategoryPiece> babyNappyMediumCategoryPieces = new ArrayList<>();
                        babyNappyMediumCategoryPieces.add(new CategoryPiece("남아용", 18));
                        babyNappyMediumCategoryPieces.add(new CategoryPiece("여아용", 19));
                        babyNappyMediumCategoryPieces.add(new CategoryPiece("남여공용", 20));
                        mCategoryPieces.add(new CategoryPiece(babyNappyMediumCategoryPieces));

                        mCategoryPieces.add(new CategoryPiece("대형"));
                        ArrayList<CategoryPiece> babyNappyBigCategoryPieces = new ArrayList<>();
                        babyNappyBigCategoryPieces.add(new CategoryPiece("남아용", 21));
                        babyNappyBigCategoryPieces.add(new CategoryPiece("여아용", 22));
                        babyNappyBigCategoryPieces.add(new CategoryPiece("남여공용", 23));
                        mCategoryPieces.add(new CategoryPiece(babyNappyBigCategoryPieces));

                        mCategoryPieces.add(new CategoryPiece("특대형"));
                        ArrayList<CategoryPiece> babyNappyBiggerCategoryPieces = new ArrayList<>();
                        babyNappyBiggerCategoryPieces.add(new CategoryPiece("남아용", 24));
                        babyNappyBiggerCategoryPieces.add(new CategoryPiece("여아용", 25));
                        babyNappyBiggerCategoryPieces.add(new CategoryPiece("남여공용", 26));
                        mCategoryPieces.add(new CategoryPiece(babyNappyBiggerCategoryPieces));

                        mCategoryPieces.add(new CategoryPiece("점보형"));
                        ArrayList<CategoryPiece> babyNappyBiggestCategoryPieces = new ArrayList<>();
                        babyNappyBiggestCategoryPieces.add(new CategoryPiece("남아용", 27));
                        babyNappyBiggestCategoryPieces.add(new CategoryPiece("여아용", 28));
                        babyNappyBiggestCategoryPieces.add(new CategoryPiece("남여공용", 29));
                        mCategoryPieces.add(new CategoryPiece(babyNappyBiggestCategoryPieces));
                        break;
                    case 1:
                        mCategoryPieces.add(new CategoryPiece("베이비 물티슈", 30));
                        break;
                    case 2:
                        mCategoryPieces.add(new CategoryPiece("베이비 클렌저/워시", 31));
                        mCategoryPieces.add(new CategoryPiece("베이비 로션/에센스", 32));
                        mCategoryPieces.add(new CategoryPiece("베이비 크림/젤(겔)/팩", 33));
                        mCategoryPieces.add(new CategoryPiece("베이비 오일", 34));
                        mCategoryPieces.add(new CategoryPiece("베이비 파우더/미스트", 35));
                        mCategoryPieces.add(new CategoryPiece("베이비 립케어/립밤", 36));
                        mCategoryPieces.add(new CategoryPiece("베이비 버터/밤/연고", 64));
                        break;
                    case 3:
                        mCategoryPieces.add(new CategoryPiece("베이비 선크림/선로션", 37));
                        mCategoryPieces.add(new CategoryPiece("베이비 선케어 기타", 38));
                        break;
                    case 4:
                        mCategoryPieces.add(new CategoryPiece("베이비 샴푸/린스", 39));
                        break;
                    case 5:
                        mCategoryPieces.add(new CategoryPiece("아이용 치약", 40));
                        break;
                    case 6:
                        mCategoryPieces.add(new CategoryPiece("기타 (베이비)", 41));
                        break;
                }
                break;
            case 1:
                switch (categoryPartId) {
//                    case 0:
//                        mCategoryPieces.add(new CategoryPiece("소형", 42));
//                        mCategoryPieces.add(new CategoryPiece("중형", 43));
//                        mCategoryPieces.add(new CategoryPiece("대형", 44));
//                        mCategoryPieces.add(new CategoryPiece("오버나이트", 45));
//                        mCategoryPieces.add(new CategoryPiece("팬티라이너", 46));
//                        mCategoryPieces.add(new CategoryPiece("체내형", 47));
//                        mCategoryPieces.add(new CategoryPiece("여성청결제", 48));
//                        break;
//                    case 1:
//                        mCategoryPieces.add(new CategoryPiece("임산부화장품", 49));
//                        break;
//                    case 2:
//                        mCategoryPieces.add(new CategoryPiece("기타 (맘/여성)", 50));
//                        break;
                    case 0:
                        mCategoryPieces.add(new CategoryPiece("임산부 화장품", 49));
                        break;
                    case 1:
                        mCategoryPieces.add(new CategoryPiece("기타 (맘/여성)", 50));
                        break;
                }
                break;
            case 2:
                switch (categoryPartId) {
                    case 0:
                        mCategoryPieces.add(new CategoryPiece("바디워시", 52));
                        mCategoryPieces.add(new CategoryPiece("핸드워시", 53));
//                        mCategoryPieces.add(new CategoryPiece("클렌징폼", 54));
                        break;
                    case 1:
                        mCategoryPieces.add(new CategoryPiece("샴푸", 55));
                        mCategoryPieces.add(new CategoryPiece("린스/컨디셔너", 56));
                        mCategoryPieces.add(new CategoryPiece("트리트먼트/팩", 57));
                        break;
                    case 2:
                        mCategoryPieces.add(new CategoryPiece("치약", 58));
//                        mCategoryPieces.add(new CategoryPiece("구강케어", 59));
                        break;
                    case 3:
                        mCategoryPieces.add(new CategoryPiece("기타 (일반)", 60));
                        break;
                }
                break;
            case 3:
                switch (categoryPartId) {
                    case 0:
                        mCategoryPieces.add(new CategoryPiece("일반 물티슈", 61));
                        break;
                    case 1:
                        mCategoryPieces.add(new CategoryPiece("탈취제/세탁세제", 62));
                        break;
                    case 2:
                        mCategoryPieces.add(new CategoryPiece("기타 (리빙)", 63));
                        break;
                }
                break;
        }
    }

    public int getNameResId() {
        return mNameResId;
    }

    public void setNameResId(int nameResId) {
        mNameResId = nameResId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getImageResId() {
        return mImageResId;
    }

    public void setImageResId(int imageResId) {
        mImageResId = imageResId;
    }

    public ArrayList<CategoryPiece> getCategoryPieces() {
        return mCategoryPieces;
    }

    public void setCategoryPieces(ArrayList<CategoryPiece> categoryPieces) {
        mCategoryPieces = categoryPieces;
    }
}
