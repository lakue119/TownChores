//package com.dn.neighborhoodchores.sample
//
//import com.dn.neighborhoodchores.model.Comment
//import com.dn.neighborhoodchores.model.Pheed
//
//val samplePheed = arrayListOf<Pheed>(
//    Pheed(
//        profileImg = "https://lh3.googleusercontent.com/proxy/KIhOi6CMta-x3PGNEgQ7O3el2qR3PDNYKfTwDmg3CvVyOdoGHxwp1aG4Fxor6MjQBrAHVvY6laIV9uiWtIUixmds79dzuUw8RUOZtgRdx08vgeiWlTSin4aJcB1qJugQhUnNG-wPy3Omx0COeEVjjZBxaK93",
//        name = "라꾸",
//        location = "신대방동",
//        title = "우리강아지랑 산책가실분",
//        content = "혹시 일자리 구하는사람 잇으면 페메 남겨주거나 해주세요 천안 \n" +
//                "성정동  이구요ㅠ 주급 50  이구   4시45분 출근 12시30분 \n" +
//                "마감 입니다 \n" +
//                "남성은 3명 다구햇구요  \n" +
//                "여성 1명  자리비여서 구합니다",
//        image = "https://www.ui4u.go.kr/depart/img/content/sub03/img_con03030100_01.jpg",
//        tags = arrayListOf(
//            "반려동물",
//            "동물",
//            "강아지",
//            "반려동물",
//            "동물",
//            "강아지",
//            "반려동물",
//            "동물",
//            "강아지",
//            "반려동물",
//            "동물",
//            "강아지"
//        ),
//        date = "1시간 전",
//        commentCount = 0
//    ),
//    Pheed(
//        profileImg = "https://newsimg.sedaily.com/2020/11/24/1ZAJ728KBG_1.jpg",
//        name = "라꾸",
//        location = "신대방동",
//        title = "우리강아지랑 산책가실분",
//        content = "혹시 일자리 구하는사람 잇으면 페메 남겨주거나 해주세요 천안 \n" +
//                "성정동  이구요ㅠ 주급 50  이구   4시45분 출근 12시30분 \n" +
//                "마감 입니다 \n" +
//                "남성은 3명 다구햇구요  \n" +
//                "여성 1명  자리비여서 구합니다",
//        image = "https://t1.daumcdn.net/liveboard/holapet/648a337a6b1f458792ef19b00e8bb31a.JPG",
//        tags = arrayListOf("반려동물", "동물", "강아지"),
//        date = "1시간 전",
//        commentCount = 0
//    ),
//    Pheed(
//        profileImg = "https://file.mk.co.kr/meet/neds/2020/12/image_readtop_2020_1274670_16076647144468147.jpg",
//        name = "라꾸",
//        location = "신대방동",
//        title = "우리강아지랑 산책가실분",
//        content = "혹시 일자리 구하는사람 잇으면 페메 남겨주거나 해주세요 천안 \n" +
//                "성정동  이구요ㅠ 주급 50  이구   4시45분 출근 12시30분 \n" +
//                "마감 입니다 \n" +
//                "남성은 3명 다구햇구요  \n" +
//                "여성 1명  자리비여서 구합니다",
//        image = "https://health.chosun.com/site/data/img_dir/2021/07/26/2021072601445_0.jpg",
//        tags = arrayListOf("반려동물", "동물", "강아지"),
//        date = "1시간 전",
//        commentCount = 0
//    ),
//    Pheed(
//        profileImg = "https://cdn.topstarnews.net/news/photo/202001/718836_431264_5120.jpg",
//        name = "라꾸",
//        location = "신대방동",
//        title = "우리강아지랑 산책가실분",
//        content = "혹시 일자리 구하는사람 잇으면 페메 남겨주거나 해주세요 천안 \n" +
//                "성정동  이구요ㅠ 주급 50  이구   4시45분 출근 12시30분 \n" +
//                "마감 입니다 \n" +
//                "남성은 3명 다구햇구요  \n" +
//                "여성 1명  자리비여서 구합니다",
//        image = "https://blog.kakaocdn.net/dn/ej7HHN/btqEpJAha97/cSWVSFX8PrV03o15PZ8Bd1/img.jpg",
//        tags = arrayListOf("반려동물", "동물", "강아지"),
//        date = "1시간 전",
//        commentCount = 0
//    ),
//)
//
//val samplePheedDetail = Pheed(
//    profileImg = "https://lh3.googleusercontent.com/proxy/KIhOi6CMta-x3PGNEgQ7O3el2qR3PDNYKfTwDmg3CvVyOdoGHxwp1aG4Fxor6MjQBrAHVvY6laIV9uiWtIUixmds79dzuUw8RUOZtgRdx08vgeiWlTSin4aJcB1qJugQhUnNG-wPy3Omx0COeEVjjZBxaK93",
//    name = "라꾸",
//    location = "신대방동",
//    title = "우리강아지랑 산책가실분",
//    content = "혹시 일자리 구하는사람 잇으면 페메 남겨주거나 해주세요 천안 \n" +
//            "성정동  이구요ㅠ 주급 50  이구   4시45분 출근 12시30분 \n" +
//            "마감 입니다 \n" +
//            "남성은 3명 다구햇구요  \n" +
//            "여성 1명  자리비여서 구합니다",
//    image = "https://www.ui4u.go.kr/depart/img/content/sub03/img_con03030100_01.jpg",
//    tags = arrayListOf(
//        "반려동물",
//        "동물",
//        "강아지"
//    ),
//    date = "1시간 전",
//    commentCount = 32
//)
//
//val sampleComment = arrayListOf<Comment>(
//    Comment(
//        parentId = 0,
//        profileImg = "https://cdn.topstarnews.net/news/photo/202001/718836_431264_5120.jpg",
//        name = "라꾸",
//        date = "2021.09.12",
//        comment = "ㅁㄴㅇㄹㅁㄴㅇㄹㅁㄴㅇㄹ",
//        childComment = arrayListOf(
//            Comment(
//                profileImg = "https://cdn.topstarnews.net/news/photo/202001/718836_431264_5120.jpg",
//                parentId = 10,
//                name = "라꾸",
//                date = "2분 전",
//                comment = "ㅁㄴㅇㄹㅁㄴㅇㄹ",
//            ),
//            Comment(
//                profileImg = "https://cdn.topstarnews.net/news/photo/202001/718836_431264_5120.jpg",
//                parentId = 10,
//                name = "라꾸",
//                date = "5분 전",
//                comment = "ㅁㄴㅇㄹㅁㄴㅇㄹ",
//            )
//        )
//    ),
//    Comment(
//        parentId = 0,
//        profileImg = "https://cdn.topstarnews.net/news/photo/202001/718836_431264_5120.jpg",
//        name = "라꾸",
//        date = "2021.09.12",
//        comment = "ㅁㄴㅇㄹㅁㄴㅇㄹㅁㄴㅇㄹ",
//    ),
//    Comment(
//        parentId = 0,
//        profileImg = "https://cdn.topstarnews.net/news/photo/202001/718836_431264_5120.jpg",
//        name = "라꾸",
//        date = "2021.09.12",
//        comment = "ㅁㄴㅇㄹㅁㄴㅇㄹㅁㄴㅇㄹ",
//        childComment = arrayListOf(
//            Comment(
//                profileImg = "https://cdn.topstarnews.net/news/photo/202001/718836_431264_5120.jpg",
//                parentId = 10,
//                name = "라꾸",
//                date = "2분 전",
//                comment = "ㅁㄴㅇㄹㅁㄴㅇㄹ",
//            ),
//            Comment(
//                profileImg = "https://cdn.topstarnews.net/news/photo/202001/718836_431264_5120.jpg",
//                parentId = 10,
//                name = "라꾸",
//                date = "5분 전",
//                comment = "ㅁㄴㅇㄹㅁㄴㅇㄹ",
//            )
//        )
//    ),
//    Comment(
//        parentId = 0,
//        profileImg = "https://cdn.topstarnews.net/news/photo/202001/718836_431264_5120.jpg",
//        name = "라꾸",
//        date = "2021.09.12",
//        comment = "ㅁㄴㅇㄹㅁㄴㅇㄹㅁㄴㅇㄹ",
//    ),
//    Comment(
//        parentId = 0,
//        profileImg = "https://cdn.topstarnews.net/news/photo/202001/718836_431264_5120.jpg",
//        name = "라꾸",
//        date = "2021.09.12",
//        comment = "ㅁㄴㅇㄹㅁㄴㅇㄹㅁㄴㅇㄹ",
//        childComment = arrayListOf(
//            Comment(
//                profileImg = "https://cdn.topstarnews.net/news/photo/202001/718836_431264_5120.jpg",
//                parentId = 10,
//                name = "라꾸",
//                date = "2분 전",
//                comment = "ㅁㄴㅇㄹㅁㄴㅇㄹ",
//            ),
//            Comment(
//                profileImg = "https://cdn.topstarnews.net/news/photo/202001/718836_431264_5120.jpg",
//                parentId = 10,
//                name = "라꾸",
//                date = "5분 전",
//                comment = "ㅁㄴㅇㄹㅁㄴㅇㄹ",
//            )
//        )
//    ),
//    Comment(
//        parentId = 0,
//        profileImg = "https://cdn.topstarnews.net/news/photo/202001/718836_431264_5120.jpg",
//        name = "라꾸",
//        date = "2021.09.12",
//        comment = "ㅁㄴㅇㄹㅁㄴㅇㄹㅁㄴㅇㄹ",
//    )
//)