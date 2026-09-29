# SailPoint Identity Security Cloud (ISC) Implementation Portfolio

Welcome! This repository showcases my technical implementation capabilities and engineering knowledge within the **Identity Governance and Administration (IGA)** domain, specifically focusing on **SailPoint Identity Security Cloud (ISC)**. 

The configurations and programming files contained here are completely sanitized, generic, production-ready portfolio pieces designed to demonstrate core architecture and access management principles.

---

## 🛠️ Repository Architecture

### 📂 /transforms (Identity Engine Configurations)
A collection of JSON transforms utilizing nested parameters and Apache Velocity templates to manage identity attributes:
*   **`calculate-samaccountname.json`**: Implements nested logic wrapping a string builder inside a lowercase text converter.
*   **`determine-lifecycle-state.json`**: An advanced script utilizing multi-variable date math (`contractStartDate` vs. `contractEndDate`) and custom conditional evaluation to map user lifecycle states (Pre-Hire ➡️ Active ➡️ Inactive ➡️ Deleted).
*   **`calculate-active-directory-ou.json`**: Executes string sanitization (`trim` and `lower`) before passing metrics to a hash table lookup for targeted Active Directory OU path mapping.
*   **`determine-contractor-end-date.json`**: Implements specialized string concatenations and conditional execution restricted solely to specific workforce categorizations.

### 📂 /rules (Custom Governance & Integration Programming)
Core backend script implementation using raw Java/BeanShell syntax native to SailPoint provisioning architectures:
*   **`WebServiceBeforeOperationRule.java`**: A comprehensive HTTP request orchestrator used by custom Web Services REST connectors. It parses raw JSON request bodies, evaluates deployment footprints, interacts dynamically with downstream endpoints via a native REST client (`executeGet`), and enforces complex multi-role business dependencies on the fly.

---

## 🧠 Core Engineering Competencies Demonstrated
*   **Decoupled SaaS Target Integrations:** Constructing customizable REST/Web Service connection scripts where pre-built native connectors are absent.
*   **Identity Lifecycle Management (JML):** Mapping cross-system attribute timing schemas into unified cloud identity configurations.
*   **Data Integrity & Cleanliness:** Writing deterministic string evaluation paths to prevent dirty data or trailing white spaces from breaking downstream directory syncs.
*   **Least Privilege & Compliance Enforcement:** Building contextual access evaluations and group dependencies directly into provisioning flows.

---

### 🔒 Compliance & Non-Disclosure Notice
All files in this repository have been written entirely from scratch using randomized mock unique identifiers, fictional system endpoints, and synthetic enterprise attributes. No real corporate data, proprietary business rules, or private infrastructure routing paths have been exposed.

