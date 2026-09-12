insert into usuarios (nome, email, senha, perfil, ativo, criado_em)
VALUES (
        'Administrador Equilibra',
        'admin@equilibra.com.br',
        '$2a$10$zXuPHRr3TtPvlKmclVIrduMYbpJrQrHEA4SdvNW2deD.pWkUO0Ija',
        'ROLE_ADMIN',
        true,
        now()
       )
on conflict (email) do nothing;