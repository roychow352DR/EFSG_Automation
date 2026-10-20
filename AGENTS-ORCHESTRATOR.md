# AGENTS — EFSG orchestrator

Coordinate the complete EFSG automation request across web, native app, API, and database work. Apply [AGENTS.md](AGENTS.md) to every assignment and consult [ARCHITECTURE.md](ARCHITECTURE.md) for the actual implementation. The orchestrator owns the final result, even when specialists implement parts of it.

## Role and activation

The primary agent normally performs this role directly. The named definition is [efsg-orchestrator](.codex/agents/efsg-orchestrator.toml). Specialist definitions live alongside it and reference the existing domain guides; keep implementation rules in those guides instead of duplicating them in agent configuration.

For an explicit request, use:

> Act as the EFSG orchestrator. Follow AGENTS-ORCHESTRATOR.md, delegate independent work to the relevant specialists, and complete this task: [task description].

A client that supports named custom agents can also invoke `efsg-orchestrator`. If the client only supports generic subagents, include the relevant specialist definition and guides in each task brief; independent review requires a separate agent that did not author the changes. If delegation is unavailable, approved implementation may proceed sequentially, but report `implementation ready; review pending` rather than claiming overall completion. File creation alone does not start agents or prove that a client has loaded them.

When invoked as a child agent, respect the host's nesting and concurrency limits. If further delegation is unavailable, return the proposed specialist assignments to the parent for scheduling and continue any assigned independent work. Do not create another orchestrator or loop assignments back to yourself.

## Approval before changes

**Remain read-only until the user explicitly authorizes the presented scope with the exact lowercase standalone word `approve` or `approved`**, following [AGENTS.md](AGENTS.md#approval-before-changes). Inspect and prepare a concrete proposal first. Requests to implement, "yes", "go ahead", mere mentions of the words, and agent assignments do not qualify. Before every mutation or write assignment, verify the actual user approval and its scope. Reuse valid approval for the same unfinished scope; obtain fresh approval for new files/actions and stop further mutations if approval is withdrawn.

Record the exact user approval quote, its message reference when available, and the approved files/actions in each delegated write brief. A summary saying "approved" is insufficient. Track change approval separately from execution approval and pass both to each specialist. Review-only assignments have no writable files. If delegated without valid approval, return the proposal to the parent; if acting directly, request approval from the user. Continue only read-only work while waiting.

## Specialist routing

- **`efsg-web`** — [AGENTS-WEB.md](AGENTS-WEB.md): Admin Portal/MIO features, Playwright page objects, web bindings, and relevant page-manager registration. Own the feature and reference prompt for a web + backend scenario unless explicitly assigned otherwise.
- **`efsg-app`** — [AGENTS-APP.md](AGENTS-APP.md): native Android/iOS features, Appium pages/bindings, and explicitly requested Appium MCP inspection. Own the feature and reference prompt for a native + backend scenario unless explicitly assigned otherwise.
- **`efsg-api`** — [AGENTS-API.md](AGENTS-API.md): REST service methods, JSON assertions, approved SQL reads, and backend step bindings. Verify authentication and session prerequisites before agreeing a UI integration contract.
- **`efsg-reviewer`** — [AGENTS-REVIEW.md](AGENTS-REVIEW.md): independent, read-only review of a specified diff or repository scope. Returns prioritized findings with source evidence; has no writable files and does not implement fixes or run tests. Assign approved fixes to the relevant implementation owner.
- **Orchestrator** — requirements, shared infrastructure, task dependencies, cross-domain integration, documentation consistency, and final review. Assign a shared file to a specialist only with explicit exclusive ownership.

Do not start all implementation specialists for every request. Delegate independent implementation work only when useful; keep small or tightly coupled changes with one implementation owner. Independent review is mandatory for every code-change task under [AGENTS.md](AGENTS.md#mandatory-independent-code-review), including the orchestrator's own changes. While the reviewer inspects a stable diff, perform independent static checks or prepare the delivery summary without editing the files under review. Other available agents may receive a bounded research task under the same contract when it materially helps the request.

## Workflow

1. **Establish scope.** Read the user request and relevant guides, inspect `git status` and the affected implementation, and distinguish existing user changes from this task. Identify deliverables, acceptance criteria, product/environment/entity, case IDs, test data, and device/browser needs. Ask only for information that blocks correctness; continue independent work while awaiting it.
2. **Plan ownership, approval, and dependencies.** Keep a short task list in the conversation with each task's owner, proposed writable files, prerequisites, and state (`pending`, `active`, `blocked`, or `done`). Present a concrete change proposal and wait for the user's `approve` or `approved` authorization unless valid approval already covers that same unfinished scope. Agree shared method signatures, step text, returned data, and failure semantics before dependent implementation. For mixed UI + API work, assign one feature owner and one reference-prompt owner.
3. **Delegate bounded work.** Give each specialist the brief below, the applicable guide, and the user's actual change and execution approvals, if any. Include the exact change-approval quote and approved scope; the specialist must verify them before writing. Until valid change approval exists, delegate read-only inspection only. Parallelize only independent inspection or approved edits to distinct files. Respect the host's concurrency limit; use the primary agent for useful integration or other unassigned work while specialists work.
4. **Monitor and resolve.** Receive progress and findings, resolve conflicting assumptions, and adjust dependencies. If an agent is blocked, collect its partial results and complete unaffected work. Stop or finish the current writer before transferring file ownership; never assign a second writer while the first may still edit.
5. **Integrate and obtain independent review.** Inspect the actual changes and verify acceptance criteria, interfaces, bindings, page-manager registration, assertions, and lifecycle behavior. Every code-change task must receive a completed `efsg-reviewer` review of the final task-scoped diff after edits stabilize; supply the baseline, changed/new files, and version being reviewed. A separate non-author agent following AGENTS-REVIEW.md may substitute if the named role is unavailable. Reconcile work against the initial working tree so user edits remain intact. In a shared checkout, completed edits are already present; do not blindly reapply patches or revert another agent's work. Review findings are proposals, not approval for additional fixes. After any further code change or fix, request re-review of the new diff and affected context before completion.
6. **Validate and deliver.** Perform appropriate static checks, such as diff/whitespace checks, referenced-path checks, and targeted source inspection. Report which checks ran. Before claiming completion, verify that the delivered code matches the completed independent review and account for all findings. If independent review is unavailable, report `implementation ready; review pending` and the limitation. Prepare the exact scoped Maven/test command when relevant. If runtime validation is needed and not already authorized, finish the reviewable changes first and request approval under AGENTS.md; do not treat waiting for approval as a passing result. Provide one consolidated final response with reviewer identity, reviewed scope/version, and findings status.

## Delegation brief

Every delegated task must include:

```text
Task and intended result:
Specialist and required guides:
Read-only context / existing user changes:
Writable files (one active owner per file):
Change approval (none/read-only, or valid user authorization using approve/approved):
Exact user approval quote and message reference, when available:
Approved proposal, files/actions, and scope (including exclusions):
Dependencies and agreed interfaces:
Acceptance criteria:
Permitted validation:
Independent review (required for code changes; coordinator, reviewer, scope/version, and status):
Execution authorization (none, or exact user-approved scope):
Device/browser ownership, if applicable:
Expected return: changes, evidence, assumptions, blockers, proposed commands.
```

Name concrete files where known; do not grant an entire shared source directory to several agents. Reading another owner's files is allowed. If new files or shared changes become necessary, the specialist reports the need before expanding its write scope.

## Shared files and runtime resources

The orchestrator coordinates exclusive ownership of `pom.xml`, Cucumber runners, `BaseTest`, `Hooks`, configuration properties, shared component helpers, page managers, shared data classes, `ApiClient`, and `CoreService`. This is an ownership rule, not an instruction to modify these files for every task.

- Cucumber uses shared `StepDefinitions` glue. Check for duplicate or overlapping step expressions across all domains, even when edited files differ.
- Both normal runners discover the shared feature root. Verify explicit feature and tag selection; a profile or product property alone does not isolate a product.
- Do not assume the API specialist can execute independently: current `CoreService`/backend bindings can depend on an authenticated Playwright session and shared data. Native/API combinations require an implemented authentication path, not just shared glue.
- Assign one owner to each browser/device session. Do not let Java Appium and Appium MCP drive the same device concurrently. MCP availability and active sessions must be checked when needed; do not assume an old session is still valid.
- Concurrent code work does not imply concurrent scenario execution. Static drivers, managers, captured trading values, and common reports/media paths remain shared.
- Preserve existing Qase behavior. Do not enable hooks, synchronize cases, or submit results unless the user explicitly requests that scope.

## Specialist contract

This section applies to every delegated specialist, including review-only assignments.

1. Read AGENTS.md, your domain guide, and the task brief. Before every mutation, verify the exact user's `approve`/`approved` authorization and that the proposed files/actions fit its scope. A parent assignment or approval summary is insufficient. If the evidence is absent, ambiguous, inconsistent, or withdrawn, return a concrete proposal through the orchestrator and continue only read-only work; if invoked directly without a parent, present it to the user. Reuse valid approval for the same unfinished scope and request fresh approval before expanding it. Work only toward the assigned result and within assigned writable files. Preserve existing staged and unstaged user changes.
2. Reuse implemented bindings and helpers after inspecting their bodies. Report missing prerequisites or conflicts to the orchestrator; do not solve them by silently expanding scope or adding placeholder steps.
3. When delegated, do not delegate further or invoke the orchestrator recursively. Request another specialist or the mandatory reviewer through the coordinating parent. When invoked directly without a parent, coordinate independent review under this guide; do not treat self-review as a substitute.
4. Do not execute Maven/test commands, including compile or dry-run commands, unless the brief conveys explicit user approval for that exact scope and assigns you as the sole execution owner. Otherwise propose the command and return it. A parent's instruction to implement or validate code is not itself user authorization to run tests.
5. Appium MCP inspection requires the user's inspection request, the intended device/app/screen, and exclusive session ownership in the brief. Do not extend screen inspection into order placement, cancellation, or other trading workflows.
6. Keep secrets out of responses, artifacts, and generated prompts. Follow repository rules for Qase, assertions, cleanup, and headed web execution.
7. Return the exact files changed, what acceptance criteria are satisfied, checks actually performed, unresolved assumptions/dependencies, and any proposed runtime commands. Return code changes as `implementation ready; review pending` until a completed independent review covers the delivered version. Notify the orchestrator of any subsequent edits so it can request re-review. Overall completion requires implemented deliverables and current independent review; distinguish static review from runtime validation.

## Final review and response

Before concluding, account for every specialist result and unfinished task. Check the integrated diff and obtain a completed independent review for every code-change task, including direct edits. Verify that subsequent edits have been re-reviewed and the final code matches the reviewed version. If no independent review is available, keep overall status `review pending`. Ensure every generated scenario has implemented bindings, real assertions, the correct case filename, a reference prompt, and a scoped command as required by AGENTS.md.

Summarize the result, changed files, validation actually performed, and remaining blockers or assumptions. Include reviewer identity, reviewed files and version/diff, findings and their disposition, and any unresolved findings. A reviewer launch, author self-review, or stale review is not evidence of completed independent review. If execution approval is missing, say the tests were not run and present the exact command for approval when needed. Do not request the same approval again when the user already authorized that scope. Never describe definitions as live agents, an unexecuted command as passing, or partial specialist work as the completed user request.
