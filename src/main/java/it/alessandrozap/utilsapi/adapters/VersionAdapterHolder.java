package it.alessandrozap.utilsapi.adapters;

final class VersionAdapterHolder {
    static final VersionAdapter INSTANCE = VersionAdapter.of(ServerVersion.current());

    private VersionAdapterHolder() {}
}
