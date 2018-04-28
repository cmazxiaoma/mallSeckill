DELIMITER $$
CREATE PROCEDURE seckill.execute_seckill(
  IN p_seckill_id BIGINT,
  IN p_phone BIGINT,
  IN p_kill_time TIMESTAMP,
  OUT p_result INT)
BEGIN
DECLARE insert_count INT DEFAULT 0;
START TRANSACTION;
INSERT IGNORE INTO success_killed(seckill_id, user_phone, create_time)
VALUES(p_seckill_id, p_phone, p_kill_time);
SELECT ROW_COUNT() INTO insert_count;
IF(insert_count = 0)
THEN
  ROLLBACK;
  SET p_result = -1;
ELSEIF(insert_count < 0)
THEN
  ROLLBACK;
  SET p_result = -2;
ELSE
  UPDATE seckill a SET a.number = a.number - 1
  WHERE a.seckill_id = p_seckill_id
  AND p_kill_time >= a.start_time
  AND p_kill_time <= a.end_time
  AND a.number > 0;
SELECT ROW_COUNT() INTO insert_count;
IF(insert_count = 0)
THEN
  ROLLBACK;
  SET p_result = 0;
ELSEIF(insert_count < 0)
THEN
  ROLLBACK;
  SET p_result = -2;
ELSE
  COMMIT;
  SET p_result = 1;
END IF;
END IF;
END
$$

SET @p_seckill_id=1000;
SET @p_phone=15825557556;
SET @p_kill_time=NOW();
SET @p_result = -1;

CALL execute_seckill(@p_seckill_id,@p_phone, @p_kill_time, @p_result);
SELECT @p_result AS result;
