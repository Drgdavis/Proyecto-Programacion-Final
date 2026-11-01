<?php

declare(strict_types=1);

class ApiException extends RuntimeException
{
    public function __construct(
        string $mensaje,
        private int $estado
    ) {
        parent::__construct($mensaje);
    }

    public function estado(): int
    {
        return $this->estado;
    }
}