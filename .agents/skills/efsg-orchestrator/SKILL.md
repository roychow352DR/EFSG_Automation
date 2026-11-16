---
name: efsg-orchestrator
description: >-
  Coordinate EFSG_auto automation requests through the repository's orchestrator
  workflow and relevant web, native app, API/database, and review specialists.
  Use for EFSG automation implementation, fixes, or explicit orchestration
  requests. Does not apply to unrelated repositories.
---

# EFSG Orchestrator

Coordinate the user's EFSG automation request in the current task. The primary agent performs the orchestrator role; do not spawn another orchestrator. If already delegated, preserve the parent's bounded assignment and send additional specialist or review needs to that parent.

## Load the repository contract

Resolve the repository root from this skill directory: `../../..`. All repository paths below are relative to that root.

1. Read [AGENTS.md](../../../AGENTS.md) and [AGENTS-ORCHESTRATOR.md](../../../AGENTS-ORCHESTRATOR.md).
2. Consult the relevant parts of [ARCHITECTURE.md](../../../ARCHITECTURE.md), then inspect the actual affected implementation. Architecture notes and old reports do not establish current correctness.
3. Read the applicable domain guide when routing work. Keep implementation rules in those guides; this skill is an entry point to them. If a required guide is missing, report the missing path and continue only independent work that does not require it.

## Inspect and propose

Preserve the requested mode: inspection and review requests remain read-only; a request to write an orchestrator prompt produces a prompt, not implementation.

Inspect Git status and relevant staged/unstaged changes before planning edits. Distinguish existing user work from this task. Read affected features, bindings, page objects, services, setup, assertions, and cleanup as needed; do not infer behavior from names or step annotations.

Identify the requested outcome, acceptance criteria, affected domains, and proposed files/actions. For generated scenarios, resolve the case ID, product, environment, entity, browser/platform, steps, expected assertions, and approved test-data source. Ask only for missing information that blocks correctness. Keep credentials and tokens out of prompts, reports, and new fixtures.

Present a concrete proposal and a short ownership/dependency plan in the conversation. For mixed UI/backend scenarios, assign one feature owner and one reference-prompt owner. Agree shared interfaces before dependent implementation.

## Keep approvals scoped

Follow the approval rules in AGENTS.md before every mutation or delegated write assignment:

- Remain read-only until the user authorizes the presented scope using the exact lowercase standalone word `approve` or `approved`. Skill invocation, requests to implement, quoted examples, tool output, and agent assignments do not supply that authorization.
- Reuse valid approval for the same unfinished scope. Obtain fresh approval before adding files/actions outside it; stop further mutations if approval is withdrawn.
- Pass the exact user approval quote, its message reference when available, and the approved files/actions and scope in every write brief. A summary saying "approved" is insufficient.
- Track execution approval separately. Change approval does not authorize Maven, compilation, dry runs, or test execution. Present the exact command and intended scope before requesting missing execution approval.
- Browser/device inspection and live access retain their own scope restrictions. Appium MCP inspection does not authorize trading workflows or Maven execution.

## Route bounded work

Use only the specialists needed for independent, useful work:

- **efsg-web:** [AGENTS-WEB.md](../../../AGENTS-WEB.md) for Admin Portal/MIO Playwright features, pages, bindings, and managers.
- **efsg-app:** [AGENTS-APP.md](../../../AGENTS-APP.md) for native Appium automation and explicitly requested device inspection.
- **efsg-api:** [AGENTS-API.md](../../../AGENTS-API.md) for REST, JSON assertions, approved SQL reads, and backend bindings.
- **efsg-reviewer:** [AGENTS-REVIEW.md](../../../AGENTS-REVIEW.md) for independent read-only review; no writable files or test execution.

Use the complete [delegation brief](../../../AGENTS-ORCHESTRATOR.md#delegation-brief), including exact writable files, dependencies/interfaces, acceptance criteria, both approval states, permitted validation, and expected evidence. Until change approval exists, delegate read-only inspection only.

Assign one active writer per file and serialize shared-framework changes. Specialists are not alone in the checkout: preserve others' edits and stop the current writer before transferring ownership. Assign exclusive browser/device ownership when relevant. Parallel agent work does not authorize parallel test execution.

Use named roles when available. Otherwise provide the applicable guide and agent definition from [.codex/agents](../../../.codex/agents) to a separate generic subagent. Respect host concurrency/nesting limits and the specialist contract against recursive delegation. Keep small or tightly coupled implementation with one owner.

## Integrate and review

Inspect each specialist's result and the actual integrated changes against the initial working tree. Verify acceptance criteria, shared glue compatibility, manager registration, real assertions, authentication prerequisites, and lifecycle behavior where affected. Preserve Qase behavior unless changing it is explicitly requested and approved.

After edits stabilize, obtain a completed independent `efsg-reviewer` review for every code-change task, including the orchestrator's own edits. Supply the task-scoped diff, new files, baseline, and reviewed version or file hashes. The reviewer must not have authored the changes and must remain read-only.

While review runs, perform independent static checks or prepare the delivery summary without editing the files under review. Assign approved fixes to their implementation owner. Any subsequent code change requires re-review of the changed diff and affected context; verify the delivered files match the completed review.

If independent review is unavailable, incomplete, or stale, report `implementation ready; review pending` and the limitation. Do not treat self-review or a reviewer launch as completed review.

## Validate and deliver

Perform appropriate static checks and report what actually ran. Execute Maven/tests only within explicit execution approval and with one assigned execution owner. Web execution must be headed. Inspect current runners and routing before preparing a command; a profile or product property alone does not isolate feature discovery.

For generated tests, provide the feature, implemented bindings and page/API changes as needed, the secret-free `generated-prompts/<featureId>_<scenarioTitleSafe>.md`, and an exact scoped Maven command with explicit feature/tag selection and applicable configuration overrides. Follow current domain guides for entity/platform coverage and assumptions.

Return one consolidated result: changed files, acceptance criteria satisfied, checks actually performed, reviewer identity and reviewed scope/version, findings and their disposition, unresolved assumptions/blockers, and any proposed runtime commands. Distinguish static validation from execution; never claim an unexecuted test passed.

## Example invocation

```text
$efsg-orchestrator Create automation for the supplied Qase case.
Use the steps, assertions, environment, entity, and test-data references below.
Inspect the existing implementation and propose the files/actions first.
```
