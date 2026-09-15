#### 事后总结及过程改进计划

##### 1. 总结范围

本项目按以下软件工程流程推进：

```text
Planning → Requirements Analysis → Design → Coding → Unit Testing
→ Official Sample Validation → Coverage / Profiling / Optimization → Final Testing
```

截至本报告编写时，Planning、需求分析、设计、编码和第一轮单元测试已经完成；官方样例验证、覆盖率、Profiler、性能优化和最终测试尚未完成。

##### 2. 已完成工作回顾

###### 2.1 Planning

开发前建立 PSP 预计时间表，对不同阶段进行任务分解和时间估计。

###### 2.2 Requirements Analysis

整理了命令行输入输出、文本预处理、相似度计算、异常处理、性能、测试、Git/GitHub 和 PSP 等要求，同时识别出重复率公式、HTML 处理和边界情况等待验证问题。

###### 2.3 Design

完成模块化设计，将程序划分为入口、应用流程、文件服务、文本预处理、相似度计算、结果格式化和异常处理等职责，并通过接口降低模块耦合。

###### 2.4 Coding

完成 Java 基础实现、Maven 构建、文件读写、文本/HTML 预处理、LCS 相似度计算、结果格式化和异常处理。

###### 2.5 Unit Testing

执行：

```powershell
mvn test
```

结果：

```text
Tests run: 23, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
Total time: 2.315 s
```

4 个测试类全部通过。

##### 3. 做得较好的地方

- **先分析、设计后编码**：降低了直接堆叠代码造成的结构性返工风险。
- **核心算法接口化**：`SimilarityCalculator` 允许后续替换算法而不必重写整个应用流程。

- **模块化单元测试**：文件服务、预处理、相似度计算和格式化分别测试，便于定位问题。
- **异常处理前置设计**：非法参数、文件读写异常等情况已纳入设计。
- **保留算法验证意识**：当前 LCS 方案及重复率公式明确作为待官方样例验证的候选实现，而不是未经验证地视为最终标准。

##### 4. 当前问题及根因

| 问题 | 根因 | 影响 |
|---|---|---|
| 官方样例尚未完整验证 | 第一轮重点放在模块单元测试 | 无法确认算法完全符合题目标准 |
| 真实样例尚未自动化回归 | 当前测试主要使用构造数据 | 算法修改后的回归成本较高 |
| 覆盖率尚无正式数据 | 尚未完成 JaCoCo 阶段 | 无法量化测试覆盖程度 |
| 尚未完成 Profiler | 尚未进入性能分析阶段 | 暂无真实热点数据 |

##### 5. 过程改进计划

###### 改进 1：尽早把官方样例纳入自动化测试

**措施：** 将 `add`、`del`、`dis_1`、`dis_10`、`dis_15`等样例纳入回归测试，并保存标准结果。

**度量：** 官方样例覆盖率达到 100%；每次核心算法修改后自动执行全部样例；存在标准结果不一致时不得进入最终提交阶段。

###### 改进 2：建立分层测试体系

后续采用：

```text
Unit Test → Integration Test → Official Sample Test → Performance Test → Regression Test
```

当前单元测试已达到 23 个，下一步重点补齐真实样例回归和集成测试。

###### 改进 3：引入覆盖率数据

配置 JaCoCo，记录 Line、Branch、Method Coverage，并针对异常处理、边界输入和核心算法分支补充测试。测试充分性不再仅用“测试数量”判断。

###### 改进 4：采用 Profiler 驱动性能优化

先建立性能基准，再用 JProfiler/规定工具定位热点函数，随后进行优化并重新测试。至少比较优化前后的总耗时、核心算法耗时和内存表现。

###### 改进 5：加强 Git 提交粒度

以后每完成一个可验证阶段就提交，例如：

```text
docs: complete requirements analysis

docs: complete design specification

feat: implement file service

feat: implement similarity calculator

test: add similarity unit tests

test: add official sample regression tests

perf: optimize similarity calculation
```

这样可以更容易定位引入问题的提交。

###### 改进 6：持续记录 PSP Actual Time

每个阶段结束后立即记录开始时间、结束时间、有效工作时间和中断时间，不在项目结束时凭记忆补填。最终计算 Estimated、Actual、Variance 和 Variance Rate。

##### 6. 下一阶段执行顺序

```text
① 官方样例验证
        ↓
② 修正算法/预处理规则（如有必要）
        ↓
③ 补充真实样例回归测试
        ↓
④ 配置并生成 JaCoCo 覆盖率
        ↓
⑤ Code Quality Analysis
        ↓
⑥ 建立性能基准
        ↓
⑦ Profiler 分析
        ↓
⑧ 针对热点进行性能优化
        ↓
⑨ 完整回归测试
        ↓
⑩ 更新 PSP Actual Time
        ↓
⑪ 更新最终测试报告
```

##### 7. 改进效果评价标准

| 方向 | 评价指标 |
|---|---|
| 需求管理 | 需求、设计、实现和测试存在对应关系 |
| 测试 | 单元测试和官方样例均可自动执行 |
| 测试充分性 | 有正式覆盖率数据 |
| 算法正确性 | 官方样例全部符合标准结果 |
| 性能 | 有优化前后对比数据 |
| 代码质量 | 代码质量检查警告得到处理 |
| PSP | Estimated / Actual / Variance 完整 |
| Git | 重要阶段具有清晰提交记录 |
| 回归 | 核心算法修改后可一键重新测试 |

##### 8. 总结

当前项目已经完成从 Planning、需求分析、设计、编码到第一轮单元测试的主要工程流程。23 个单元测试全部通过，说明当前实现具备良好的基础测试条件。

但单元测试通过不能代替官方样例验证。下一阶段的首要任务应是使用真实样例验证相似度算法，再依据覆盖率和 Profiler 数据进行针对性的测试补充和性能优化。

通过上述改进，可以将项目从“能够运行的程序”进一步完善为具有需求依据、设计文档、自动化测试、性能数据、PSP 记录和可回归验证能力的软件工程项目。
