#可以拷贝复制到python控制台执行

import redis

# 创建 Redis 连接对象
redis_client = redis.StrictRedis(host='localhost', port=6379, db=0)

# 列出所有内容相关的 测试阶段删除垃圾数据
keys = redis_client.keys('*content*')  # '*'表示匹配所有键
print("要删除的键:", keys)

# 删除所有的键
deleted_count = redis_client.delete(*keys)
print("已删除的键数量:", deleted_count)
