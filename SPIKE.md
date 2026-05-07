# SPIKE Documentation: Migration from Batch Sync to Real-Time API

---

## 1. Objective

Enable **real-time data access from RxClaim** for RxPricing users by replacing the existing **hourly batch sync mechanism**.

### Goal:

* Eliminate data latency (up to 1 hour)
* Ensure **up-to-date pricing data availability**
* Improve system responsiveness and user experience

---

## 2. Current System Behavior (Batch-Based Sync)

### End-to-End Flow

1. **Scheduled Sync Job**

    * Runs every **1 hour**

2. **Delta Identification**

   ```sql
   changeDateTime BETWEEN lastRunDateTime AND currentDateTime
   ```

3. **Data Extraction**

    * Extract programs generate multiple files based on detected changes

4. **Wrapper Job Execution**

    * **BK1** → Submitted Job
    * **BKA** → ROBOT Job

5. **File Transfer**

    * BK1 → **CFT + ECG**
    * BKA → **Tidal + ECG**

6. **File Packaging**

    * Files compressed into a **ZIP**

7. **Cloud Upload**

    * ZIP uploaded to **Azure Blob Storage**

8. **Event Trigger**

    * Azure Function triggers on file upload:

      ```
      /rxclaim/ecg/receiveFromRxclaim/responseFile/{filename}
      ```

9. **Integration Processing**

    * File is processed
    * Data stored in **MongoDB (Golden Copy collections)**

---

## 3. Problem Statement

### Data Latency Issue

**Example Scenario:**

* Last sync: **11:00 PM**
* Data change in RxClaim: **11:05 PM**
* Data available in RxPricing: **12:00 AM**
    **Latency: ~55 minutes**

### Impact

* Users operate on **stale data**
* Real-time workflows are **blocked**
* Causes delays in **pricing updates**
* Reduced system reliability for time-sensitive operations

---

## 4. Proposed Solution (Real-Time API-Based Approach)

Replace the batch-based sync mechanism with **on-demand API calls**.

### Key Concept

* RxPricing directly calls APIs exposed by **RxApiHub**
* RxApiHub fetches **real-time data from RxClaim**
* Eliminates dependency on:

    * Scheduled jobs
    * File transfers

---

## 5. Proposed Architecture

```
RxPricing  --->  RxApiHub API  --->  RxClaim (Source of Truth)
```

### Flow

1. User performs action in **RxPricing**
2. RxPricing invokes **RxApiHub API**
3. RxApiHub fetches data from **RxClaim in real-time**
4. Response returned immediately
5. RxPricing:

    * Uses response directly OR
    * Persists selectively in MongoDB (if required)

---

## 6. Impacted Collections (Golden Copy)

The following **35 collections** are currently updated via batch sync:

### Core Pricing Collections

* rxClaimDrugCostSchedule
* rxClaimDrugCostScheduleDetails
* rxClaimDrugCostComparisonSchedule
* rxClaimDrugCostComparisonScheduleDetails
* rxClaimPriceTable
* rxClaimPriceSchedule
* rxClaimPriceScheduleCriteriaDetails

### Response File Collections

* rxClaimRCSNDPResponseFile
* rxClaimRCSN2PResponseFile
* rxClaimRCSN5PResponseFile
* rxClaimRCSORPResponseFile
* rxClaimRCMCLPResponseFile
* rxClaimRCMLIPResponseFile
* rxClaimRCPHNPResponseFile
* rxClaimRCSNHPResponseFile
* rxClaimRCTAXPResponseFile
* rxClaimRCFSHPResponseFile
* rxClaimRCFSCPResponseFile
* rxClaimRCSNNPResponseFile
* rxClaimRCPSNPResponseFile
* rxClaimRCGPSPResponseFile

### Error Collections

* rxClaimRCDC3PError
* rxClaimRCDC4PError
* rxClaimRCDC5PError
* rxClaimRCDC6PError
* rxClaimRCPRCPError
* rxClaimRCPSCPError
* rxClaimRCPTFPError
* rxClaimRCSN2PError
* rxClaimRCSN5PError
* rxClaimRCSNDPError
* rxClaimRCSORPError
* rxClaimRCSNNPError
* rxClaimRCPSNPError
* rxClaimRCGPSPError

---

## 7. API Impact Analysis Requirement

To enable the migration, we must **identify all APIs performing read/write operations** on the above collections.

### Scope:

* All repositories/services:

    * **Integration**
    * **Experience**
    * Any other dependent modules

---

## 8. Sample API Analysis

### Repository: Integration

### API:
```java
@PostMapping("/rxclaim/integration/createRxApiHubRequestFile/{ticketId}")
```

---

### 8.1. API Purpose

* Generates **JSON request files** for RxApiHub
* Uses **multiple Golden Copy collections**

---

### 8.2. Collections (golden copies) Usage Mapping

#### A. Direct Fetch (Using `ticketId`)

| Code Reference                                | Collection                    | Operation | Input    |
| --------------------------------------------- | ----------------------------- | --------- | -------- |
| `generateRCSNDPResponseFileDTOList(ticketId)` | **rxClaimRCSNDPResponseFile** |  READ    | ticketId |
| `generateRCSN2PResponseFileDTOList(ticketId)` | **rxClaimRCSN2PResponseFile** |  READ    | ticketId |
| `generateRCSN5PResponseFileDTOList(ticketId)` | **rxClaimRCSN5PResponseFile** |  READ    | ticketId |
| `generateRCSORPResponseFileDTOList(ticketId)` | **rxClaimRCSORPResponseFile** |  READ    | ticketId |

---

#### B. Environment Lookup

| Code Reference                            | Collection           | Operation | Input                |
| ----------------------------------------- | -------------------- | --------- | -------------------- |
| `getRxClaimEnvById(rxclaimEnvironmentId)` | **rxclaimEnvMaster** |  READ    | rxclaimEnvironmentId |

---

#### C. Derived Fetch (Using `snhSuperNetworkId`)

| Code Reference                                                                    | Collection                    | Operation | Input Source |
| --------------------------------------------------------------------------------- | ----------------------------- | --------- | ------------ |
| `generateRCSNHPResponseFileDTOList(rcsN2PResponseFileDTO.getSnhSuperNetworkId())` | **rxClaimRCSNHPResponseFile** |  READ    | From RCSN2P  |
| `generateRCSNHPResponseFileDTOList(rcsN5PResponseFileDTO.getSnhSuperNetworkId())` | **rxClaimRCSNHPResponseFile** |  READ    | From RCSN5P  |
| `generateRCSNHPResponseFileDTOList(rcsNDPResponseFileDTO.getSnhSuperNetworkId())` | **rxClaimRCSNHPResponseFile** |  READ    | From RCSNDP  |
| `generateRCSNHPResponseFileDTOList(rcsORPResponseFileDTO.getSnhSuperNetworkId())` | **rxClaimRCSNHPResponseFile** |  READ    | From RCSORP  |
| `getRCSNHPResponseFileDTO(superNetworkId, ...)`                                   | **rxClaimRCSNHPResponseFile** |  READ    | Derived      |

---

#### D. Derived Fetch (Using `phnPharmacyNetworkId`)

| Code Reference                                                                       | Collection                    | Operation | Input Source |
| ------------------------------------------------------------------------------------ | ----------------------------- | --------- | ------------ |
| `generateRCPHNPResponseFileDTOList(rcsN5PResponseFileDTO.getPhnPharmacyNetworkId())` | **rxClaimRCPHNPResponseFile** |  READ    | From RCSN5P  |
| `generateRCPHNPResponseFileDTOList(rcsNDPResponseFileDTO.getPhnPharmacyNetworkId())` | **rxClaimRCPHNPResponseFile** |  READ    | From RCSNDP  |
| `generateRCPHNPResponseFileDTOList(rcsORPResponseFileDTO.getPhnPharmacyNetworkId())` | **rxClaimRCPHNPResponseFile** |  READ    | From RCSORP  |
| `getRCPHNPResponseFileDTO(pharmacyNetworkId, ...)`                                   | **rxClaimRCPHNPResponseFile** |  READ    | Derived      |

---

### 8.3. Key Observations

#### Read vs Write

*  All operations in this API are **READ**
* No WRITE (insert/update/delete) observed on any collection

---

#### Risk for Real-Time Migration

* API depends on **multiple collections in sequence**
* Each step relies on:

    * Output of previous query
* Tight coupling with **Golden Copy structure**

---

## 9. Next Steps (Analysis Only)

### 9.1. API Identification

* Identify all APIs across relevant repositories (e.g., **Integration**, **Experience**, etc.) that:

    * Perform **READ operations** on Golden Copy collections
    * Perform **WRITE operations** on Golden Copy collections

---

### 9.2. Mapping

For each identified API, create a clear mapping of:

* **API Endpoint**
* **Collection(s) Accessed**
* **Operation Type**

    * READ (fetch/query)
    * WRITE (insert/update/delete)
