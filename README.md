```mermaid
flowchart TD

%% 공통 인증 처리
    Start((Start))
    Start --> Auth["X-MEMBER-ID 인증"]
    Auth --> AuthCheck{"인증 성공 여부"}
    AuthCheck -- "아니오" --> Error401["401 Unauthorized"] --> EndErr((End))
    AuthCheck -- "예" --> Route["요청 분기"]

%% GRI 전체 조회
    Route --> GriAll["GRI 전체 조회 요청"]
    GriAll --> GriAllSvc["griDisclosureService.getAll()"]
    GriAllSvc --> GriAllResp["전체 목록 반환"]
    GriAllResp --> End1((End))

%% GRI 단건 조회
    Route --> GriOne["GRI 단건 조회 요청"]
    GriOne --> GriOneSvc["griDisclosureService.getByCode()"]
    GriOneSvc --> GriOneResp["단건 항목 반환"]
    GriOneResp --> End2((End))

%% GRI 항목 등록
    Route --> GriCreate["GRI 항목 등록 요청"]
    GriCreate --> GriCreateSvc["griDisclosureService.create()"]
    GriCreateSvc --> GriCreateResp["등록 완료"]
    GriCreateResp --> End3((End))

%% GRI 항목 수정
    Route --> GriUpdate["GRI 항목 수정 요청"]
    GriUpdate --> UpdateSvc["소유자 확인 후 수정"]
    UpdateSvc --> GriUpdateResp["수정 완료"]
    GriUpdateResp --> End4((End))

%% GRI 항목 삭제
    Route --> GriDelete["GRI 항목 삭제 요청"]
    GriDelete --> DeleteSvc["소유자 확인 후 삭제"]
    DeleteSvc --> GriDeleteResp["삭제 완료"]
    GriDeleteResp --> End5((End))

%% GRI 진행률 조회
    Route --> GriProgress["GRI 진행률 조회 요청"]
    GriProgress --> ProgressSvc["진행률 계산 로직"]
    ProgressSvc --> GriProgressResp["진행률 반환"]
    GriProgressResp --> End6((End))

%% 색상 스타일 정의
    classDef forest fill:#e6f4ea,stroke:#2e7d32,stroke-width:1.5px,color:#2e7d32;
    classDef terminal fill:#d0f0c0,stroke:#1b5e20,color:#1b5e20;
    classDef error fill:#fdecea,stroke:#c62828,color:#c62828;

%% 클래스 적용 (한 줄로 유지)
    class Start,End1,End2,End3,End4,End5,End6,EndErr terminal;
    class Auth,AuthCheck,Route,GriAll,GriAllSvc,GriAllResp,GriOne,GriOneSvc,GriOneResp,GriCreate,GriCreateSvc,GriCreateResp,GriUpdate,UpdateSvc,GriUpdateResp,GriDelete,DeleteSvc,GriDeleteResp,GriProgress,ProgressSvc,GriProgressResp forest;
    class Error401 error;
```
