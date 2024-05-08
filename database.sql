CREATE DATABASE sinhvienform
GO

USE sinhvienform
GO

CREATE TABLE sinhviensimple 
(
    MaSinhVien VARCHAR(20) PRIMARY KEY,
    HoTen NVARCHAR(200),
    -- GioiTinh AS (CASE WHEN GioiTinhBit=1 THEN 'True' ELSE 'False' END),
    GioiTinh NVARCHAR(3) CHECK (GioiTinh = N'NAM' OR GioiTinh = N'NỮ'),
    -- NgaySinh AS FORMAT (NgaySinhDate, 'dd/MM/yyyy'),
    NgaySinh DATE,
    MaLop VARCHAR(20)
);
