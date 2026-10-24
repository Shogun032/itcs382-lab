Files read (all under `parcelpoint/src/main/java/parcelpoint/`): `Locker.java`, `Main.java`, `Parcel.java`, `ParcelService.java`; locations below are relative to that directory.

| No. | Check | Code location (file:line) | Finding | Severity | Suggested fix |
|---:|---|---|---|---|---|
| 1 | 1 | ParcelService.java:42 | `==` compares String identities, so pickup can fail even when the supplied code has the same text. | Must fix | Compare codes using `equals()` or `Objects.equals()`. |
| 2 | 1 | ParcelService.java:76–79 | The `FileWriter` is never flushed or closed, which can leave output unwritten and leak file handles. | Must fix | Create the writer in a try-with-resources block. |
| 3 | 1 | ParcelService.java:80–81 | The empty catch block silently discards persistence failures while store and pickup still report success. | Must fix | Propagate or explicitly report the save failure. |
| 4 | 1 | Main.java:13,17 | Command arguments are indexed and parsed without validation, so missing arguments or nonnumeric values terminate the program. | Must fix | Check argument counts and handle invalid integers before invoking the service. |
| 5 | 1 | Main.java:11 | Splitting on a single space produces empty arguments for repeated spaces and does not separate tab-delimited input. | Should fix | Split trimmed, nonempty input using `\\s+`. |
| 6 | 1 | Parcel.java:9–12; ParcelService.java:26,56–61 | Size is not restricted to 1–3: zero or negative sizes can be stored, and `fee()` treats every other value as large. | Must fix | Reject sizes outside 1–3 in the `Parcel` constructor. |
| 7 | 1 | ParcelService.java:24–29,39–47 | Storage permits duplicate codes, while pickup identifies a parcel only by code, making duplicate parcels ambiguous. | Should fix | Reject codes already assigned to an active parcel. |
| 8 | 1 | ParcelService.java:78 | Codes are written into CSV without escaping; a code containing a comma creates extra columns. | Should fix | Escape CSV fields or reject delimiter characters in parcel codes. |
| 9 | 1 | ParcelService.java:6 | The `Date` import is unused. | Note | Remove the import. |
| 10 | 1 | ParcelService.java:12 | `maxParcels` is never read or used to enforce a limit. | Note | Remove the field unless an actual capacity check is required. |
| 11 | 1 | Main.java:8–27 | The `Scanner` is never closed; it wraps `System.in`, so closing it also requires an explicit ownership decision. | Note | If `main` owns standard input for the application's lifetime, close the scanner when the command loop ends. |
| 12 | 1 | Main.java:6–27 | `main()` combines input parsing, five command branches, service calls, and output formatting, making it harder to read and extend. | Should fix | Extract a small handler for each command. |
| 13 | 2 | Main.java:6–27 | **Logical method cohesion:** one method selects among different command operations according to command text. | Should fix | Move each command operation into its own handler. |
| 14 | 2 | ParcelService.java:24–36 | **Procedural method cohesion:** `storeParcel()` combines locker allocation, status changes, notification formatting, and file persistence. | Should fix | Keep allocation and state changes here and extract notification and persistence operations. |
| 15 | 2 | ParcelService.java:39–49 | **Procedural method cohesion:** `pickUp()` coordinates lookup, locker release, status changes, saving, and fee calculation as one procedure. | Should fix | Extract the pickup state transition and delegate fee calculation and saving. |
| 16 | 2 | ParcelService.java:64–71 | **Logical method cohesion:** `printParcels()` selects between staff reporting and waiting-parcel reporting using a flag. | Should fix | Provide separate methods for the two reports. |
| 17 | 2 | ParcelService.java:24–81 | **Weak class cohesion:** `ParcelService` represents parcel operations, notification output, report presentation, and CSV persistence. | Should fix | Extract CSV persistence and presentation from the service. |
| 18 | 2 | ParcelService.java:52–61 | **Weak class cohesion:** `fee()` uses only its arguments and none of the service's attributes. | Should fix | Move the calculation to a small fee-policy operation or to the parcel domain model. |
| 19 | 2 | ParcelService.java:46,52–56 | **Stamp coupling:** `pickUp()` passes a whole `Parcel` to `fee()`, which reads only its size. | Should fix | Pass the size instead of the parcel. |
| 20 | 2 | Main.java:14–15; ParcelService.java:33 | **Stamp coupling:** `Main` receives a whole mutable `Locker` solely to read its number. | Should fix | Return the allocated locker number or a small allocation result. |
| 21 | 2 | Main.java:19,21; ParcelService.java:64–68 | **Control coupling:** `Main` passes a boolean that chooses the service's reporting behavior. | Should fix | Replace the boolean-controlled method with explicitly named report methods. |
| 22 | 2 | Parcel.java:7; ParcelService.java:28,44,67–68,78 | **Content coupling and exposed state:** `status` is public, and the service directly reads and changes another class's representation. | Should fix | Make status private and expose controlled transitions and a read accessor. |
| 23 | 2 | ParcelService.java:20–21; Main.java:22 | **Common coupling through shared mutable state:** `getLockers()` returns the actual internal list, allowing callers to add, remove, or reorder service lockers. | Should fix | Return an unmodifiable snapshot or expose only the required query. |
| 24 | 2 | Main.java:22 | **Strong coupling through reach-through:** `Main` obtains the service's locker collection and performs its own traversal and availability query. | Should fix | Add a `countFreeLockers()` operation to `ParcelService`. |
| 25 | 2 | Parcel.java:6,9; Locker.java:5,8; ParcelService.java:15–17,26,56–61; Main.java:13 | **Connascence of convention:** these classes must agree that integer values 1, 2, and 3 mean ordered parcel and locker sizes. | Should fix | Introduce a shared size enum with an explicit fit rule. |
| 26 | 2 | Parcel.java:13; ParcelService.java:28,44,68 | **Connascence of convention:** the classes coordinate lifecycle state through exact strings such as `"NEW"` and `"STORED"`. | Should fix | Replace status strings with a shared enum. |
| 27 | 2 | Main.java:13; Parcel.java:9–12 | **Connascence of position:** adjacent `String` constructor arguments represent code and student ID, so swapping them compiles but changes meaning. | Should fix | Use distinct value types for parcel code and student ID. |

Check 1: **12 findings**. String literals repeated many times: **not found**.

Check 2: **15 findings**. Additional procedural-or-weaker methods, excessive class dependency counts, and additional stamp, control, common, or content interactions: **not found**.