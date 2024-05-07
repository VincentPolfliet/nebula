@import dev.vinpol.spacetraders.sdk.models.Ship
@import java.util.Collection

@param Collection<Ship> ships

<div class="container is-widescreen">
    <div class="section">

        <h1 class="title">Fleet</h1>

        <table class="table">
            <thead>
            <tr>
                <th scope="col">Name</th>
                <th scope="col">Role</th>
                <th scope="col">Location</th>
                <th scope="col">System</th>
                <th scope="col">Status</th>
                <th scope="col"></th>
            </tr>
            </thead>
            <tbody>
            @for(Ship ship : ships)
                <tr>
                    <td>${ship.getSymbol()}</td>
                    <td>${ship.getRole()}</td>
                    <td>${ship.getNav().getWaypointSymbol()}</td>
                    <td><a href="/galaxy/system/${ship.getNav().getSystemSymbol()}">${ship.getNav().getSystemSymbol()}</a></td>
                    <td>${ship.getNav().getStatus()}</td>
                    <td>
                        // TODO actions
                    </td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
</div>
