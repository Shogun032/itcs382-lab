Read: `bikeshare-poor/src/main/java/BikeShare.java` (the only Java source file under the requested directory). Locations below refer to this file.

| No. | Check | Code location (file:line) | Finding | Severity (Must fix, Should fix or Note) | Suggested fix |
| --- | --- | --- | --- | --- | --- |
| 1 | 1 | BikeShare.java:47–50,59–60 | Public `ride()` assumes a non-null array with at least four non-null elements; callers outside `main()` can cause unchecked exceptions. | Should fix | Make `ride()` private if it is only intended for the validated command loop. |
| 2 | 1 | BikeShare.java:47–94 | `ride()` spans 48 lines and combines station lookup, two command branches, validation, pricing, file output, state changes and console output. | Should fix | Extract separate rental and return methods, with a helper for station lookup. |
| 3 | 1 | BikeShare.java:11–13,67,70–72,85–87 | Three parallel arrays represent each bike, requiring matching lengths and coordinated updates at every index. | Should fix | Replace the parallel arrays with a collection of bike objects. |
| 4 | 1 | BikeShare.java:11,13,64,72,78–85 | Numeric codes mix rental state, member categories and fee rates, making their meanings difficult to distinguish. | Should fix | Introduce named constants for these codes and rates. |
| 5 | 1 | BikeShare.java:67–68 | The CSV separator literal `","` is repeated five times in one record-building expression. | Note | Use `String.join()` to assemble the CSV fields. |
| 6 | 2 | BikeShare.java:15–44 | **Procedural method cohesion:** `main()` groups help display, input handling, command validation, dispatch and error reporting around execution order. | Should fix | Extract help display and command processing into focused methods. |
| 7 | 2 | BikeShare.java:47–94 | **Logical method cohesion:** `ride()` groups rental and return operations selected by the `returning` flag. | Should fix | Split the method into `rent()` and `returnBike()`. |
| 8 | 2 | BikeShare.java:9–94 | Weak class cohesion: `BikeShare` represents fleet and rental state while also implementing the console interface, pricing and CSV storage. | Should fix | Move console handling and CSV writing into separate classes while retaining rental state and operations together. |
| 9 | 2 | BikeShare.java:10–13 | All four fields are public mutable static arrays; callers can replace them or alter their contents, breaking state and index invariants. | Must fix | Make the arrays private and expose only the operations callers need. |
| — | 1 | BikeShare.java:1–95 | Unused imports, unused fields, empty blocks, object comparisons using `==`, and unclosed resources: **not found**; the scanner and writer use try-with-resources at lines 19 and 65. | Note | No fix needed for these checks. |
| — | 2 | BikeShare.java:1–95 | Interactions tighter than data coupling between application classes, excessive application-class dependencies, reach-through calls, and weakenable **between-class** connascence of name, type, position, convention or algorithm: **not found**; only one application class exists. | Note | No fix identified for these checks. |
| — | 2 | BikeShare.java:15–94 | Operations returning an internal collection: **not found**; both methods return `void`, although fields expose arrays directly as reported above. | Note | No additional fix needed. |

Findings: **Check 1: 5. Check 2: 4.** “Not found” rows are excluded from the counts.
