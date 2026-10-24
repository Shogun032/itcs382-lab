# Lab Report, Code Quality and Design Quality

Repository URL: https://github.com/Shogun032/itcs382-lab.git

## Tools

| Item | Version or name |
| --- | --- |
| JDK | 21.0.7 |
| Maven | 3.10.0; current Maven runtime: Java 27.0.1.0.2 |
| IDE | VS Code 1.132.0 |
| SonarQube for IDE | 6.2.0 |
| AI tool and exact model | Codex GPT 6.1 Sol Light for the reported generation/review chats; coding/report assistance details are listed in the AI usage declaration. |

## Task 1. SonarQube on the starter code

Commit of the starter, before any change: 95ff783

Table 1. SonarQube issues in the starter code

| No. | Rule | File:line | Message |
| --- | --- | --- | --- |
| 1 | java:S2143 | ParcelService.java:1 | Use the "java.time" API for date and time. |
| 2 | java:S1068 | ParcelService.java:12 | Remove this unused "maxParcels" private field. |
| 3 | java:S106 | ParcelService.java:30 | Replace this use of System.out by a logger. |
| 4 | java:S106 | ParcelService.java:67 | Replace this use of System.out by a logger. |
| 5 | java:S106 | ParcelService.java:69 | Replace this use of System.out by a logger. |
| 6 | java:S108 | ParcelService.java:80 | Remove this block of code, fill it in, or add a comment explaining why it is empty. |

## Task 2. AI review of the starter code

| Item | Value |
| --- | --- |
| Date | 24 Oct 2026 |
| AI tool and exact model | Codex GPT-6.1 Sol light |
| Commit reviewed | 95ff783 |
| How the AI read the files (attached the files, opened the folder, or pasted the files) | I attached a ZIP file containing the starter code and pasted Prompt A into the chat. |
| Output file in the reviews folder | reviews/parcelpoint-review.md |

Table 2. All findings on the starter code

| No. | File:line | Finding | Found by (SonarQube, AI or both) | SonarQube rule or course term | Correct? (yes or no, and why) |
| --- | --- | --- | --- | --- | --- |
| 1 | ParcelService.java:42 | String codes are compared using ==. | both | String identity comparison | Yes; == compares references, so equal text from separate input lines can fail to match. |
| 2 | ParcelService.java:76–79 | FileWriter is never flushed or closed. | both | Resource management | Yes; there is no close(), flush(), or try-with-resources, so buffered data may remain unwritten. |
| 3 | ParcelService.java:80–81 | The empty catch block hides saving failures. | both | java:S108; exception handling | Yes; IOException is caught without reporting or recovery. |
| 4 | Main.java:13,17 | Command arguments and numeric input are not validated. | AI | Input validation | Yes; direct array indexing and Integer.parseInt can throw uncaught exceptions. |
| 5 | Main.java:11 | Splitting on one space mishandles repeated spaces and tabs. | AI | Input parsing | Yes; split(" ") can produce empty arguments and does not split tabs. |
| 6 | Parcel.java:9–12; ParcelService.java:26,52–61 | Parcel size is not restricted to 1–3. | AI | Input validation | Yes; the constructor accepts any integer, nonpositive sizes fit lockers, and fee() treats other sizes as large. |
| 7 | ParcelService.java:24–29,39–49 | Duplicate parcel codes are allowed. | AI | Identifier ambiguity | Yes; storage has no uniqueness check although pickup uses a code to identify a parcel. |
| 8 | ParcelService.java:78 | CSV fields are written without escaping. | AI | CSV serialization | Yes; an accepted code containing a comma creates an extra CSV column. |
| 9 | ParcelService.java:6 | The Date import is unused. | AI | Unused import | Yes; Date is imported but never referenced. The Java-extension warning is excluded from SonarQube results. |
| 10 | ParcelService.java:12 | The maxParcels field is unused. | both | java:S1068; unused field | Yes; the field is initialized but never read or used to enforce a limit. |
| 11 | Main.java:8–27 | The Scanner is never closed. | AI | Resource ownership | Yes; main creates the Scanner but never closes it. Closing it would also close System.in, so ownership matters. |
| 12 | Main.java:6–27 | main combines parsing, command dispatch, service calls, and output. | AI | Method complexity | Yes; all command handling is in one method, although its length alone does not establish excessive complexity. |
| 13 | Main.java:6–27 | main has logical cohesion through command selection. | AI | Logical cohesion | Yes; command text selects among several distinct operations within the same method. |
| 14 | ParcelService.java:24–37 | storeParcel is classified as procedurally cohesive. | AI | Procedural cohesion (disputed) | No, not established; allocation, state changes, notification, and persistence all support one store operation on the same parcel. Mixed responsibilities are real, but sequencing alone does not establish procedural cohesion. |
| 15 | ParcelService.java:39–50 | pickUp is classified as procedurally cohesive. | AI | Procedural cohesion (disputed) | No, not established; lookup, release, status update, saving, and fee calculation support one pickup operation. The review does not justify why this is merely procedural cohesion. |
| 16 | ParcelService.java:64–72 | printParcels selects between two report behaviors using a flag. | AI | Logical cohesion | Yes; forStaff selects a full staff report or a waiting-parcel report. |
| 17 | ParcelService.java:24–82 | ParcelService mixes business operations, presentation, and persistence. | AI | Weak class cohesion | Yes; allocation and pickup rules, console messages, reports, and FileWriter persistence reside in one class. |
| 18 | ParcelService.java:52–62 | fee does not use service attributes and is called a class-cohesion problem. | AI | Weak class cohesion (disputed) | No, not sufficient evidence; the calculation is stateless but directly belongs to parcel-service behavior. Not using instance fields alone does not prove weak cohesion. |
| 19 | ParcelService.java:46,52–56 | Passing Parcel to fee is reported as inter-class stamp coupling. | AI | Stamp coupling (scope disputed) | No as an interaction between classes; pickUp and fee are methods of ParcelService. Passing only size would narrow the parameter, but this call itself does not cross a class boundary. |
| 20 | Main.java:14–15; ParcelService.java:33 | Main receives a whole Locker only to read its number. | AI | Stamp coupling | Yes; Main depends on the Locker object even though the result it displays is only the locker number. |
| 21 | Main.java:19,21; ParcelService.java:64–68 | A boolean from Main controls reporting behavior. | AI | Control coupling | Yes; true and false select different operations inside ParcelService.printParcels. |
| 22 | Parcel.java:7; ParcelService.java:28,44,67–68,78 | Public parcel status exposes representation to the service. | AI | Content coupling; exposed state | Yes; ParcelService reads and writes Parcel.status directly, bypassing controlled transitions. |
| 23 | ParcelService.java:20–21; Main.java:22 | getLockers exposes a mutable list, described as common coupling. | AI | Encapsulation leak; common coupling (disputed) | Yes for the exposure; it returns the actual mutable list. No for the strict common-coupling label: this is instance state exposed through a getter, not shared global data, and Main currently only reads it. |
| 24 | Main.java:22 | Main reaches into the service locker collection to count free lockers. | AI | Reach-through coupling | Yes; Main knows the collection representation and Locker.isFree instead of requesting a service-level count. |
| 25 | Parcel.java:6,9; Locker.java:5,8; ParcelService.java:15–17,26,56–61; Main.java:13 | Classes share numeric size meanings and ordering. | AI | Connascence of convention | Yes; integer values 1–3 and their ordering must agree across parsing, parcel sizes, locker sizes, fitting, and fees. |
| 26 | Parcel.java:13; ParcelService.java:28,44,68 | Lifecycle states depend on agreed string values. | AI | Connascence of convention | Yes; state assignments and comparisons depend on exact status spellings rather than a restricted status type. |
| 27 | Main.java:13; Parcel.java:9–12 | Two adjacent String constructor arguments rely on position. | AI | Connascence of position | Yes; swapping code and studentId compiles but changes the parcel data meaning. |
| 28 | ParcelService.java:1 (reported); import at :6 | SonarQube requests java.time instead of the legacy date API. | SonarQube | java:S2143 | Yes; the legacy Date import is present, but unused, so removing it is sufficient. This differs from the AI finding that the import is unused. |
| 29 | ParcelService.java:30 | System.out is used for the notification message. | SonarQube | java:S106 | Yes; the reported System.out call exists. Whether to keep intentional CLI output is a separate Task 3 decision. |
| 30 | ParcelService.java:67 | System.out is used for the staff report. | SonarQube | java:S106 | Yes; the reported System.out call exists. Staff output is intentional CLI behavior and can be assessed in Task 3. |
| 31 | ParcelService.java:69 | System.out is used for the waiting-parcel report. | SonarQube | java:S106 | Yes; the reported System.out call exists. Report output is intentional CLI behavior and can be assessed in Task 3. |

The AI rows follow findings 1–27 in `reviews/parcelpoint-review.md`; rows 28–31 add SonarQube-only findings. “Found by” includes the initial SonarQube analysis in Table 1 and the later analysis that additionally reported String comparison and FileWriter closure. These two later findings existed in the starter; their complete rule IDs were not recorded. Java-extension warnings are excluded. Repeated occurrences of S106 remain separate because they have different locations. The unused Date import and the legacy-date API rule are different findings even though removing the same import resolves both.

2.1 Name two problems that only the AI review found.

The AI found missing argument-count and integer-input validation at `Main.java:13,17`. It also found control coupling at `Main.java:19,21` and `ParcelService.java:64–68`, where a boolean selects the reporting behavior. Neither appears in the recorded SonarQube results; String comparison and FileWriter closure were also reported by SonarQube in the later analysis and are therefore not AI-only findings.

2.2 Name one problem that only SonarQube found, or write none.

SonarQube reported `java:S106` for direct `System.out` use at `ParcelService.java:30,67,69`. The AI discussed mixed presentation responsibilities, but did not report these calls as a logger-replacement issue.

2.3 Name one AI finding that was wrong or too vague, and explain why. If every finding was right, explain how you checked them.

AI finding 19 describes the call from `pickUp()` to `fee()` as stamp coupling between classes. Both methods are defined in `ParcelService`, so this particular call is within one class and does not establish the inter-class interaction requested by Prompt A. Passing only the parcel size would still reduce the information passed to `fee()`, but that useful suggestion does not make the classification correct. I checked the method definitions and call site at `ParcelService.java:39–56`; the remaining findings were checked against the four Java source files, with qualifications recorded in Table 2.

## Task 3. Fix and check

Table 3. What you did with each SonarQube issue in Table 1

| No. in Table 1 | Fixed or kept | Commit, or the reason for keeping it |
| --- | --- | --- |
| 1 | Fixed | ef29b1b |
| 2 | Fixed | b602510 |
| 3 | Kept | System.out intentionally displays the parcel storage notification to the CLI user. |
| 4 | Kept | System.out intentionally displays the staff report requested by the CLI user. |
| 5 | Kept | System.out intentionally displays waiting parcels requested by the CLI user. |
| 6 | Fixed | f8ca278 |

Number of SonarQube issues that remain after your fixes: 3 (all are intentionally kept java:S106 occurrences in SonarQube for IDE).

Additional fixes for findings shown in the later SonarQube analysis:

| Finding (starter location) | Change | Commit |
| --- | --- | --- |
| String comparison, ParcelService.java:42 | Compare parcel codes with equals() instead of ==. | 6d06079 |
| Unclosed FileWriter, ParcelService.java:76–79 | Use try-with-resources to flush and close the writer. | 30d8810 |

The two design problems that you fixed

| Design problem (course term) | What you changed | Commit |
| --- | --- | --- |
| Control coupling (Table 2, row 21) | Replace printParcels(boolean) with printStaffReport() and printWaitingParcels(), and update Main. | 8cbc87a |
| Encapsulation leak and reach-through coupling (Table 2, rows 23–24) | Replace getLockers() with countFreeLockers() so Main no longer accesses the internal collection. | 5df1fd2 |

Output of the program after your fixes, for the commands `store P1 6501 2`, `pickup P1 5` and `staff`, and the content of parcels.csv:

```text
Commands: store <code> <studentId> <size 1-3>, pickup <code> <days>, list, staff, quit
SMS to 6501: parcel P1 is in locker 2
Locker 2
Fee: 30.0
P1 6501 PICKED_UP
Free lockers: 3
```

Content of `parcelpoint/parcels.csv`:

```text
P1,PICKED_UP
```

Validation: Maven package succeeded after each of the four additional fixes. The original command sequence produced the output above. The list/staff check showed the waiting parcel before pickup, no waiting parcel after pickup, and free lockers changing from two to three. A separate run in a temporary directory with `parcels.csv` deliberately made a directory confirmed that saving failure produces `UncheckedIOException` with the message `Failed to save parcels.csv`. Generated CSV and target files remain ignored.

## Task 4. AI-generated poor and good designs

| Item | bikeshare-poor | bikeshare-good |
| --- | --- | --- |
| Date | 24 Oct 2026 | 24 Oct 2026 |
| AI tool and exact model | Codex GPT 6.1 Sol Light (user-reported) | Codex GPT 6.1 Sol Light |
| Prompt (B or C as given, or your changed prompt in full) | Prompt B as given in prompts.md, with the complete BikeShare specification (the opening station/setup sentence and items 1–6) pasted into the specification placeholder. | Prompt C (user-reported); confirm the complete specification and any prompt changes |
| Builds with mvn package (yes or no) | Yes; BUILD SUCCESS on 24 Oct 2026 | Yes; user-provided BUILD SUCCESS on 24 Oct 2026 at 22:12:27 +07:00 |
| Number of classes, interfaces, enums and records | 1 class (BikeShare), 0 interfaces, 0 enums, 0 records | 5 classes, 1 interface, 2 enums, 0 records |
| SonarQube issues, total | 18 | 3 (analysis coverage of all 8 Java files remains to be confirmed) |
| SonarQube issues, by rule | java:S106 × 9; java:S2386 × 4; java:S1220 × 1; java:S1172 × 1; java:S135 × 1; java:S3776 × 1; java:S3358 × 1 | java:S1172 × 1; java:S106 × 1; java:S135 × 1 |
| AI review findings in Check 1 | 5 | 1 |
| AI review findings in Check 2 | 4 | 2 |
| Review output file in the reviews folder | reviews/bikeshare-poor-review.md | reviews/bikeshare-good-review.md |


Poor-version Prompt A review: Check 1 contains 5 findings and Check 2 contains 4 findings; “not found” rows are excluded. The complete review is saved in `reviews/bikeshare-poor-review.md`, with Markdown table formatting repaired and the review wording preserved. Review date: 24 Oct 2026. AI tool and exact model: Codex GPT 6.1 Sol Light.

Poor-version SonarQube results: SonarQube for IDE reported 18 findings for BikeShare.java. ParcelPoint findings are excluded from this total. Additional locations such as `[+14 locations]` are not counted as separate findings. BikeShare rule locations: S1220 at line 1; S2386 at lines 10–13; S1172 at line 15; S106 at lines 16, 17, 18, 36, 38, 42, 73, 88, 92; S135 at line 20; S3776 at line 47; S3358 at line 79.

Poor-version validation: The supplied code built without modification. The user ran student rental and return (bike 1, 10 minutes, 20 baht), staff rental and return (bike 5, 10 minutes, 30 baht), and attempted a third rental at Dorm after renting its two bikes (reported no bikes available). Returns to a different station succeeded. The resulting `rides.csv` was inspected and contained:

```text
1,6501,1,Canteen,10,20
5,S1,2,Library,10,30
```

No build-error repair was needed. These checks establish the tested behavior, not the absence of all bugs or design problems.

Good-version Prompt A review: Check 1 contains 1 finding and Check 2 contains 2 findings; “not found” rows are excluded. The complete review is saved in `reviews/bikeshare-good-review.md`. Review date: 24 Oct 2026. AI tool and exact model: Codex GPT 6.1 Sol Light.

Good-version SonarQube results: SonarQube for IDE reported S1172 at Main.java:8 (unused args), S106 at Main.java:12 (System.out), and S135 at ConsoleMenu.java:21 (multiple break/continue statements). The additional locations under S135 belong to the same finding. ParcelPoint findings are excluded. Analysis coverage of all eight Java files remains to be confirmed.

Good-version validation: The user built the supplied Prompt C project without code repairs and tested the same student and staff rentals, cross-station returns, and exhausted Dorm inventory. Fees were 20 baht for the 10-minute student ride and 30 baht for the 10-minute staff ride. The third Dorm rental was rejected with `Invalid input: No bikes available at DORM.` The actual `bikeshare-good/rides.csv` was inspected and contained:

```text
1,"6501",10,20
5,"S1",10,30
```

The CSV layout differs from the poor version but records both completed rides. SonarQube for IDE results are recorded above, with full-file analysis coverage still to be confirmed. Prompt A review counts are recorded above; successful runtime checks do not establish design quality.

4.1 Did SonarQube report any cohesion or coupling problem in bikeshare-poor? Quote the rule and the message if it did.

SonarQube did not explicitly classify a method as having weak cohesion or an interaction as a particular coupling type. It did report a related encapsulation problem: `java:S2386`, “Make this member "protected".”, on the four public static array fields at `BikeShare.java:10–13`. Exposing mutable state allows callers to depend on and modify the internal representation, which can increase coupling. It also reported `java:S3776`, “Refactor this method to reduce its Cognitive Complexity from 25 to the 15 allowed.”, at `BikeShare.java:47`; this measures method complexity rather than directly identifying cohesion or coupling.

4.2 Did the AI review tell the two versions apart? Give one finding for bikeshare-poor that does not appear for bikeshare-good.

Yes. The poor-version review identified weak class cohesion in `BikeShare.java:9–94`, because one class combines fleet and rental state, console interaction, pricing, and CSV storage. The good-version review did not report this problem: these responsibilities are separated among `BikeShareService`, `Bike`, `ConsoleMenu`, `MemberType`, and `CsvRideStore`, with persistence accessed through `RideStore`. The reviews reported 5 code-quality and 4 design-quality findings for poor, compared with 1 and 2 for good; the remaining good-version findings show that the design is not problem-free.

4.3 Which version would you rather extend with a new member type, such as a visitor who pays 4 baht per minute, and why?

I would extend bikeshare-good because member categories and fee rates are centralized in `MemberType`. Adding `VISITOR(4)` would allow the existing enum parser and fee calculation to support the new category, while the console help text would also need updating; the service and CSV writer would not need a new pricing branch. In bikeshare-poor, I would need to update integer type codes, member-type parsing, the fee conditional, and help text inside the combined class. The good version therefore keeps this change smaller and makes it easier to test without changing unrelated rental or persistence behavior.

## Task 5. Reflection

5.1 What does SonarQube check, and what does it not check? Answer with examples from this lab.

SonarQube detects specific code issues, such as unused fields and unclosed resources. It does not fully judge design quality, such as whether a class combines too many responsibilities.

5.2 When should you not trust the AI review? Give an example from this lab.

I should not trust an AI finding without checking the code. For example, it described pickUp() calling fee() as coupling between classes, although both methods belong to ParcelService.

5.3 What will you do differently in Project Phase 2 after this lab?

In Project Phase 2, I will separate responsibilities and keep internal data private. I will use SonarQube and AI reviews, verify their findings, and test each fix before committing it.

## AI usage declaration

| AI tool and exact model | Purpose | Files or parts that it produced or changed | How you checked its output |
| --- | --- | --- | --- |
| Codex (GPT-6, as identified by this session; exact UI variant not independently verified) | Validate findings, assist with Task 3 fixes, and draft report answers including Task 5 | LAB_REPORT.md; ParcelService.java; Main.java | Compared findings to source; built after each fix; exercised pickup, reporting, CSV output, and a saving-failure case. |
| Codex GPT 6.1 Sol Light (user-reported) | Generate the poor-design BikeShare version using Prompt B | bikeshare-poor/pom.xml; bikeshare-poor/src/main/java/BikeShare.java | Maven build succeeded; tested student and staff fees, cross-station returns, unavailable bikes, and inspected rides.csv. |
| Codex GPT 6.1 Sol Light | Generate the good-design BikeShare version using Prompt C on 24 Oct 2026 | bikeshare-good/pom.xml; the eight Java files under bikeshare-good/src/main/java/bikeshare/ | Maven build succeeded; tested student and staff fees, cross-station returns, unavailable bikes, and inspected rides.csv. |
| Codex GPT 6.1 Sol Light | Review bikeshare-poor using Prompt A on 24 Oct 2026 | reviews/bikeshare-poor-review.md | Recorded the full review and counted 5 Check 1 findings and 4 Check 2 findings, excluding not-found rows; comparison is documented in Task 4. |
| Codex GPT 6.1 Sol Light | Review bikeshare-good using Prompt A on 24 Oct 2026 | reviews/bikeshare-good-review.md | Recorded the full review and counted 1 Check 1 finding and 2 Check 2 findings, excluding not-found rows; comparison is documented in Task 4. |
