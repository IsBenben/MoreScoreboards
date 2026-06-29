# More Scoreboards

## 介绍

本MC模组增加了更多计分板准则，用于在定制服务器组合不同的计分板，提供个性化排名等。

## 核心功能

### 新准则

- `[命名空间]_by_tag:[名称]`：统计信息的标签版。例如，`minecraft.mined_by_tag:minecraft.base_stone_overworld`
  表示某玩家挖掘主世界石头的个数之和。
- `[命名空间]_reversed:[名称]`：与标签版类似，为标签下统计项目的所有玩家的值之和，而不是玩家在标签下所有统计项目的值的和。

### 特殊占位符

- `morescoreboards.total`：用于`[命名空间]_by_tag:[名称]`的名称部分，表示该统计信息下的所有项目之和。

## 示例

```mcfunction
/scoreboard objectives add used minecraft.used_by_tag:morescoreboards.total "使用物品榜"
```

## License

本项目以MIT License协议授权。
