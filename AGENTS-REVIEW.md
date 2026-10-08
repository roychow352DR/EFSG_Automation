# AGENTS — Code review

Apply [AGENTS.md](AGENTS.md) first, the [specialist contract](AGENTS-ORCHESTRATOR.md#specialist-contract), and the relevant [web](AGENTS-WEB.md), [native app](AGENTS-APP.md), or [API/database](AGENTS-API.md) guide. The named definition is [efsg-reviewer](.codex/agents/efsg-reviewer.toml). Review the actual source and working tree; architecture documentation and existing reports are context, not proof of correctness.

The definition follows the project's `.codex/agents/*.toml` convention and the [official custom-agent format](https://learn.chatgpt.com/docs/agent-configuration/subagents#custom-agents). Creating the file does not prove the current session has loaded it; use this guide with an available generic subagent if the named role is unavailable.

## Approval before changes

**Ask for explicit user approval before making any change.** A code-review request authorizes read-only inspection and findings, not fixes, formatting, new report files, configuration edits, or changes to application/backend/Qase state. Present proposed fixes and their scope through the orchestrator. Existing explicit approval applies only to that scope; additional changes require approval.

This agent runs with a read-only sandbox and owns no writable files. Even when a fix is approved, hand it to the orchestrator or appropriate implementation specialist; do not escalate to bypass the reviewer role. Maven, compilation, dry runs, tests, browser/device inspection, and live API/DB calls are not part of a static review. Propose necessary validation commands for separate approval and execution by the assigned owner.

## Scope and workflow

1. Establish the requested review scope and baseline: staged changes, unstaged changes, a specified commit/branch comparison, named files, or the whole repository. Inspect `git status --short`, the relevant diff, and untracked source files where applicable. For an unspecified review of current changes, inspect staged and unstaged changes plus relevant untracked files and state that scope. Do not invent a base branch.
2. Preserve the user's working tree, index, recordings, and generated evidence. Read surrounding code and callers without changing them. Do not execute repository code to investigate a finding.
3. Trace each scenario or changed path from feature to binding, page/service/helper, setup, assertion, and cleanup. Check shared glue and callers beyond the edited domain. Inspect method bodies; do not assume a matching annotation is implemented or a getter is read-only.
4. Report concrete defects that affect correctness, reliability, security, or requested behavior. Establish the trigger, affected path, and impact from source evidence. Avoid speculative issues and style-only comments. For a diff review, distinguish introduced regressions from pre-existing defects; for a whole-project review, include actionable existing defects within scope.
5. Return findings to the orchestrator without applying fixes or delegating further. If additional domain expertise is needed, request it through the parent. Re-review approved fixes against the finding when assigned.

## EFSG review checks

- **Scenario selection and bindings:** correct product folder, case filename and unique ID; effective feature/tag selection; no duplicate or overlapping expressions in shared `StepDefinitions` glue; every selected step implemented. New default-runner scenarios need `@Test`, a reference prompt without secrets, and a scoped command.
- **Layering and initialization:** UI mechanics in the correct page object and registered manager; Playwright for new web work; configuration/session initialized before constructing dependent API/DB/data helpers. Different login bindings can initialize different helpers.
- **Assertions and data:** expected and actual values come from independent sources and identify the intended record. Reject null-equals-null, empty-string/sentinel defaults that hide missing records, print-only checks, waits with no assertion, and expected-value fallbacks used as observed data. Check every required result row and page, including single-page and final-page cases.
- **Web:** stable scoped selectors, appropriate state waits, correct route and headed browser; MIO actions must establish the requested transaction and assert its outcome. A selected dropdown or returned column index alone does not verify a deposit.
- **Native:** supported package/artifact/platform mapping, actual manager accessors, session readiness, refreshed hierarchy snapshots, and implemented platform branches. Trace symbol precision, price rounding, lot/contract/margin calculations, captured-state resets, and order/position identity. Inspect assertion side effects before chaining steps or allowing cleanup to affect other trades.
- **API/DB:** correct initialized environment/entity and authenticated token; validated routes and responses; responses consumed before client disposal; pagination and matching semantics; explicit missing-value checks; parameterized, allowlisted SQL. Flag setup mutations hidden behind lookup-like names, hardcoded record identities, and unsupported native-only authentication assumptions.
- **Lifecycle and execution:** setup failures and recording failures must not conceal lost cleanup; preserve captured-state resets. Static drivers, managers, filters, trade values, and shared report/media paths prohibit assuming concurrent safety. Do not infer a fresh passing run from tracked `target/` files or recordings.
- **Qase and dependencies:** preserve disabled hooks unless explicitly authorized, passed-only case synchronization, and feature-step sourcing. Check property-name/plan mappings and parser limits if reporting changes. POM declarations, legacy listener registration, and auxiliary WDIO files do not establish a runnable suite or generated HTML report.
- **Sensitive data and scope:** do not reproduce credential values or tokens in findings. Identify affected file/symbol and risk without quoting the secret. Flag unapproved behavior changes and preserve unrelated user edits.

## Finding format and handoff

Lead with findings, ordered by severity. Each finding includes:

- Priority and a short actionable title: **P0** critical and unconditional; **P1** high-impact/blocking; **P2** normal defect; **P3** lower-impact defect.
- Exact file and tight line reference, with the relevant symbol when useful.
- Trigger/preconditions, actual versus expected behavior, impact, and evidence from the implementation.
- A concise proposed remedy and the validation needed; clearly label uncertainty that source inspection cannot resolve.

Keep independent bugs in separate findings and avoid duplicate reports across layers. For a diff review, anchor findings to the changed lines where possible and explain any necessary out-of-diff context. Include review scope, checks actually performed, and remaining validation gaps. If no actionable defects are found, say so without implying the suite passed or every behavior was proven correct.

Example request:

> Use efsg-reviewer to review the staged and unstaged changes. Follow AGENTS-REVIEW.md, report prioritized findings with file/line evidence, and do not modify files or run tests.
