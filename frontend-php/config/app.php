<?php

declare(strict_types=1);

return [
    'gateway_url' => rtrim(
        getenv('GATEWAY_URL') ?: 'http://localhost:8080',
        '/'
    ),
];