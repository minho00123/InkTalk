# InkTalk

<p align="center">
<img src="https://github.com/minho00123/InkTalk/blob/dev/inkTalk/src/resources/images/logo.png" width="300px" height="300px" alt="InkTalk logo">
</p>

<p align="center">
Java WebSocket을 이용해 여러 사용자가 동시에 협업할 수 있는 화이트보드 기반 회의 도구 프로그램입니다.
</p>

# 📌 Table of Contents
- [🎬 시연 영상](#-시연-영상)
- [🛠 기술 스택](#-기술-스택)
- [💡 동기](#-동기)
- [⚙️ 설계 및 준비 과정](#-설계-및-준비-과정)
- [🔥 도전](#-도전)
- [📝 회고](#-회고)

# 🎬 시연 영상
![InkTalk_preview](https://github.com/user-attachments/assets/62d1d1c0-4871-4268-9c2c-f9133ec14275)

# 🛠 기술 스택

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Java Swing](https://img.shields.io/badge/Java-Swing-blue)
![Oracle DB](https://img.shields.io/badge/Oracle-Database-brightgreen?logo=oracle)

# 💡 동기

저희 팀은 자바 소켓을 주제로 미니 프로젝트를 진행하면서, "어떻게 하면 더 효율적으로 소통할 수 있을까?"라는 질문에서 출발했습니다. 단순한 텍스트 기반의 채팅만으로는 실시간 협업 과정에서 모든 아이디어를 효과적으로 전달하기 어렵다고 느꼈고, 특히 시각적인 정보나 복잡한 개념을 설명할 때 한계를 경험했습니다.

이에 저희는 실시간으로 그림을 그리며 아이디어를 공유할 수 있는 화이트보드 기능을 추가하면, 더 직관적이고 몰입감 있는 커뮤니케이션이 가능하리라는 판단을 내렸습니다. 이러한 문제의식과 아이디어를 바탕으로, 저희는 여러 사용자가 동시에 접속하여 자유롭게 그림을 그리고 대화할 수 있는 협업용 화이트보드 회의 도구를 개발하게 되었습니다.

이 프로젝트는 단순히 기술 구현을 넘어서, 실제 협업 환경에서의 불편함을 해소하고자 하는 고민에서 시작되었으며, 사용자 중심의 실용적인 도구를 만들고자 하는 목표를 가지고 진행되었습니다.

# ⚙️ 설계 및 준비 과정

프로젝트를 구현하기 전, 개발 흐름을 명확히 하고 효율적으로 작업을 진행하기 위해 다양한 도구를 활용하여 기초 설계를 진행했습니다. 이 과정을 통해 전체적인 구조를 구상하고, 구현 과정에서의 혼선을 줄일 수 있었습니다.

### 🎨 Figma를 활용한 와이어프레임 구축 (👉 [Figma 보러가기](https://www.figma.com/design/mufch5UlaeOqB9pG5LNF1l/White-Board-with-JAVA?node-id=0-1&t=SFu1bdGLg2urSXHU-1))

Figma를 이용하여 프로젝트의 기본 디자인을 시각화하였습니다.
- 로그인/회원가입 화면을 구성해 사용자별 기능 구분을 구현하였고,
- 화이트보드 기능에는 펜, 펜 색상 변경, 지우개, 전체 지우기, 파일 저장 등의 요소를 포함했습니다.
- 채팅 기능은 각 사용자마다 고유 색상으로 표시되도록 하여 가독성과 사용자 인식을 높였습니다.
<img width="814" alt="Figma Image" src="https://github.com/user-attachments/assets/3a1c47e2-50dc-4ff3-b72f-2f4bd6d8a4e0" />

### 🗂 ER 다이어그램을 통한 데이터베이스 설계 (👉 [ERD 보러가기](https://www.erdcloud.com/d/Lc6Xk8t6QKQpiNTHR))

기초적인 데이터 흐름을 이해하고 구현을 명확히 하기 위해 ER 다이어그램을 작성했습니다.
- 비록 간단한 프로젝트였지만, 기초적인 데이터베이스 설계 능력을 기르는 데에 의미 있는 작업이었습니다.
- 회원 정보와 같은 핵심 데이터의 구조를 미리 구상함으로써 구현 시 방향을 잡는 데 도움이 되었습니다.
<img width="864" alt="ERD Image" src="https://github.com/user-attachments/assets/77445458-d1e8-4b8c-890c-37662b02291d" />

### 📋 구글 스프레드시트를 통한 기능 명세서 작성 (👉 [구글 스프레드시트 보러가기](https://docs.google.com/spreadsheets/d/18K1yl-flL5tHtxQhPD-GmYSRtft5GXbACi-LjaFek6M/edit?usp=sharing))

구현해야 할 기능들을 구체적으로 정리하고, 우선순위에 따라 개발 순서를 조율하기 위해 구글 스프레드시트를 활용했습니다.
- 각 기능을 세부 항목별로 명확히 정의하고,
- 구현 난이도와 중요도를 고려하여 우선순위 기반 개발 전략을 수립하였습니다.
- 특히 제한된 시간 안에 진행되는 팀 프로젝트에서 이러한 정리는 개발 효율을 높이는 데 큰 도움이 되었습니다.
<img width="1295" alt="Google Spreadsheet Image" src="https://github.com/user-attachments/assets/06ddab8a-7352-4612-a7d7-57e269c34d47" />

# 🔥 도전
다수의 사용자가 서버에 접속해 한 캔버스에서 그림을 그리게 되면서 동시 수정 에러를 뜻하는 ConcurrentModificationException이 발생했습니다. 

이는 사용자가 그린 한 획을 의미하는 Stoke들을 for문을 돌면서 읽어주는 과정에서 계속해서 다른 사용자가 획들을 추가하며 List의 수정이 일어나 발생하는 것이었습니다.
이 Stroke는 ArrayList에 담겨 For each문을 돌고 있었기 때문에 중간에 수정이 발생할 경우 반복을 멈추며 에러를 발생시키고 있었습니다.

## 해결 방법
이를 해결하기 위해 두가지 해결 방법을 시도해 보았습니다.

### 첫번째 방법
여러 스레드가 동시에 공유된 리스트를 수정하면서 발생하는 문제였기 때문에 synchronized()와 리스트에 Collections.synchronizedList() 메서드를 사용해 공유 자원을 동기화 하고자 하였습니다. 하지만 다중 사용자의 복잡한 사용을 시행하는 경우에는 list에 접근하거나 순회하는 모든 코드를 동기화 처리해야했기 때문에 코드가 간결하지 않고 유지보수가 쉽지 않았습니다.
### 두번째 방법
그렇기 때문에 List 의 구현체로 ArrayList가 아닌 CopyOnWriteArrayList를 사용해 수정 작업이 발생할 때마다 원본 데이터를 새 배열로 복사해 작업하도록 하여 내부 데이터를 읽는 작업에서 문제가 발생하지 않게 되었습니다.

해당 자료구조는 수정 작업보다 읽기 작업을 하는 경우가 많을 때 더 적합하기 때문에 적절한 방법이었습니다. 


# 📝 회고
이번 프로젝트를 통해 저는 Stream과 Socket의 개념을 익히고 사용해볼 수 있는 기회를 가질 수 있었습니다. 
이전까지는 WebSocket만을 사용해봤기 때문에 socket통신의 제약과 특징에 대해 이해할 수 있는 기회였습니다. 그 중 특히 소켓 통신에서 수신과 송신이 각각의 독립적인 흐름으로 움직이고 있기 때문에 연결 관리와 더불어 공유 자원에 대한 접근제어의 중요성을 알 수 있었습니다.

다양한 배경을 가진 팀원들과 프로젝트를 함께하며 말하지 않으면 모르는 채로 넘어갈 수밖에 없다는 사실 또한 깨달을 수 있었습니다. 한정된 시간에서 완성도 높은 결과를 내기 위한 충분한 함의의 중요성을 알 수 있는 기회였습니다.

마지막으로 이번 프로젝트를 통해 소켓 통신에 대한 흥미와 이해도가 더욱 높아지며,
나아가서 시간 기반 버퍼링 방식을 도입해 네트워크 부담을 줄이거나, 유니캐스트 기능을 통해 귓속말 채팅을 처리하는 등의 다양한 리팩토링 아이디어를 얻을 수 있었습니다.
 
